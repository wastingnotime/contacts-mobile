package transporthttp

import (
	"encoding/json"
	"errors"
	"fmt"
	"io"
	"log/slog"
	"net/http"
	"strings"
	"time"

	"github.com/wastingnotime/contacts-mobile/server/internal/application"
	"github.com/wastingnotime/contacts-mobile/server/internal/domain"
)

type Server struct {
	mux    *http.ServeMux
	logger *slog.Logger
}

func NewServer(service *application.Service, apiPrefix string, logger *slog.Logger) (*Server, error) {
	if service == nil {
		return nil, fmt.Errorf("contacts service must not be nil")
	}

	normalizedPrefix := normalizePathPrefix(apiPrefix)
	if normalizedPrefix == "" {
		return nil, fmt.Errorf("contactsBffApiPrefix must not be blank")
	}
	if logger == nil {
		logger = noopLogger()
	}

	handler := &handler{service: service, logger: logger}
	mux := http.NewServeMux()
	mux.HandleFunc("GET /health/live", handler.live)
	mux.HandleFunc("GET /health/ready", handler.ready)
	mux.HandleFunc("GET "+normalizedPrefix+"/contacts", handler.listContacts)
	mux.HandleFunc("GET "+normalizedPrefix+"/contacts/{id}", handler.loadContact)
	mux.HandleFunc("POST "+normalizedPrefix+"/contacts", handler.createContact)
	mux.HandleFunc("PUT "+normalizedPrefix+"/contacts/{id}", handler.updateContact)
	mux.HandleFunc("DELETE "+normalizedPrefix+"/contacts/{id}", handler.deleteContact)

	return &Server{mux: mux, logger: logger}, nil
}

func (s *Server) Handler() http.Handler {
	return http.HandlerFunc(func(responseWriter http.ResponseWriter, request *http.Request) {
		startedAt := time.Now()
		recorder := &statusRecordingResponseWriter{ResponseWriter: responseWriter, statusCode: http.StatusOK}
		s.mux.ServeHTTP(recorder, request)

		if strings.HasPrefix(request.URL.Path, "/health/") {
			return
		}

		s.logger.InfoContext(
			request.Context(),
			"http request completed",
			"method", request.Method,
			"path", request.URL.Path,
			"status_code", recorder.statusCode,
			"duration_ms", time.Since(startedAt).Milliseconds(),
		)
	})
}

type handler struct {
	service *application.Service
	logger  *slog.Logger
}

func (h *handler) live(responseWriter http.ResponseWriter, request *http.Request) {
	writeJSON(responseWriter, http.StatusOK, map[string]string{"status": "alive"})
}

func (h *handler) ready(responseWriter http.ResponseWriter, request *http.Request) {
	if err := h.service.Ready(request.Context()); err != nil {
		h.logger.WarnContext(request.Context(), "readiness check failed", "error", err)
		http.Error(responseWriter, "service not ready", http.StatusServiceUnavailable)
		return
	}

	writeJSON(responseWriter, http.StatusOK, map[string]string{"status": "ready"})
}

func (h *handler) listContacts(responseWriter http.ResponseWriter, request *http.Request) {
	contacts, err := h.service.ListContacts(request.Context())
	if err != nil {
		writeError(responseWriter, err)
		return
	}
	if contacts == nil {
		contacts = []domain.Contact{}
	}
	writeJSON(responseWriter, http.StatusOK, contacts)
}

func (h *handler) loadContact(responseWriter http.ResponseWriter, request *http.Request) {
	contact, err := h.service.LoadContactByID(request.Context(), request.PathValue("id"))
	if err != nil {
		writeError(responseWriter, err)
		return
	}
	writeJSON(responseWriter, http.StatusOK, contact)
}

func (h *handler) createContact(responseWriter http.ResponseWriter, request *http.Request) {
	draft, err := decodeDraft(request)
	if err != nil {
		writeError(responseWriter, err)
		return
	}
	contact, err := h.service.CreateContact(request.Context(), draft)
	if err != nil {
		writeError(responseWriter, err)
		return
	}
	writeJSON(responseWriter, http.StatusCreated, contact)
}

func (h *handler) updateContact(responseWriter http.ResponseWriter, request *http.Request) {
	draft, err := decodeDraft(request)
	if err != nil {
		writeError(responseWriter, err)
		return
	}
	contact, err := h.service.UpdateContact(request.Context(), request.PathValue("id"), draft)
	if err != nil {
		writeError(responseWriter, err)
		return
	}
	writeJSON(responseWriter, http.StatusOK, contact)
}

func (h *handler) deleteContact(responseWriter http.ResponseWriter, request *http.Request) {
	if err := h.service.DeleteContact(request.Context(), request.PathValue("id")); err != nil {
		writeError(responseWriter, err)
		return
	}
	responseWriter.WriteHeader(http.StatusNoContent)
}

func decodeDraft(request *http.Request) (domain.ContactDraft, error) {
	var payload contactDraftPayload
	decoder := json.NewDecoder(request.Body)
	decoder.DisallowUnknownFields()
	if err := decoder.Decode(&payload); err != nil {
		return domain.ContactDraft{}, fmt.Errorf("decode contact payload: %w", err)
	}

	draft := payload.toDomain()
	if err := validateDraft(draft); err != nil {
		return domain.ContactDraft{}, err
	}
	return draft, nil
}

func validateDraft(draft domain.ContactDraft) error {
	if strings.TrimSpace(draft.FirstName) == "" {
		return fmt.Errorf("first_name must not be blank")
	}
	if strings.TrimSpace(draft.LastName) == "" {
		return fmt.Errorf("last_name must not be blank")
	}
	if strings.TrimSpace(draft.PhoneNumber) == "" {
		return fmt.Errorf("phone_number must not be blank")
	}
	return nil
}

func writeJSON(responseWriter http.ResponseWriter, statusCode int, payload any) {
	responseWriter.Header().Set("Content-Type", "application/json")
	responseWriter.WriteHeader(statusCode)
	_ = json.NewEncoder(responseWriter).Encode(payload)
}

func writeError(responseWriter http.ResponseWriter, err error) {
	switch {
	case errors.Is(err, domain.ErrContactNotFound):
		http.Error(responseWriter, err.Error(), http.StatusNotFound)
	case strings.Contains(err.Error(), "must not be blank"):
		http.Error(responseWriter, err.Error(), http.StatusBadRequest)
	case strings.Contains(err.Error(), "decode contact payload"):
		http.Error(responseWriter, err.Error(), http.StatusBadRequest)
	default:
		http.Error(responseWriter, err.Error(), http.StatusInternalServerError)
	}
}

type contactDraftPayload struct {
	FirstName   string `json:"first_name"`
	LastName    string `json:"last_name"`
	PhoneNumber string `json:"phone_number"`
}

func (payload contactDraftPayload) toDomain() domain.ContactDraft {
	return domain.ContactDraft{
		FirstName:   payload.FirstName,
		LastName:    payload.LastName,
		PhoneNumber: payload.PhoneNumber,
	}
}

func normalizePathPrefix(prefix string) string {
	normalized := strings.TrimSpace(prefix)
	normalized = strings.TrimRight(normalized, "/")
	if normalized == "" {
		return ""
	}
	if !strings.HasPrefix(normalized, "/") {
		normalized = "/" + normalized
	}
	return normalized
}

type statusRecordingResponseWriter struct {
	http.ResponseWriter
	statusCode int
}

func (w *statusRecordingResponseWriter) WriteHeader(statusCode int) {
	w.statusCode = statusCode
	w.ResponseWriter.WriteHeader(statusCode)
}

func noopLogger() *slog.Logger {
	return slog.New(slog.NewTextHandler(io.Discard, nil))
}
