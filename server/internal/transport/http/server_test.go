package transporthttp

import (
	"context"
	"errors"
	"io"
	"net/http"
	"net/http/httptest"
	"strings"
	"testing"

	"github.com/wastingnotime/contacts-mobile/server/internal/application"
	"github.com/wastingnotime/contacts-mobile/server/internal/domain"
)

type fakeRepository struct{}

func (fakeRepository) ListContacts(ctx context.Context) ([]domain.Contact, error) {
	return []domain.Contact{{ID: "1", FirstName: "Ada", LastName: "Lovelace", PhoneNumber: "123"}}, nil
}

func (fakeRepository) LoadContactByID(ctx context.Context, id string) (domain.Contact, error) {
	if id != "1" {
		return domain.Contact{}, domain.ErrContactNotFound
	}
	return domain.Contact{ID: "1", FirstName: "Ada", LastName: "Lovelace", PhoneNumber: "123"}, nil
}

func (fakeRepository) CreateContact(ctx context.Context, draft domain.ContactDraft) (domain.Contact, error) {
	return domain.Contact{ID: "created", FirstName: draft.FirstName, LastName: draft.LastName, PhoneNumber: draft.PhoneNumber}, nil
}

func (fakeRepository) UpdateContact(ctx context.Context, id string, draft domain.ContactDraft) (domain.Contact, error) {
	return domain.Contact{ID: id, FirstName: draft.FirstName, LastName: draft.LastName, PhoneNumber: draft.PhoneNumber}, nil
}

func (fakeRepository) DeleteContact(ctx context.Context, id string) error {
	return nil
}

func (fakeRepository) Ready(ctx context.Context) error {
	return nil
}

func TestServerRoutesContactsEndpoints(t *testing.T) {
	service := application.NewService(fakeRepository{})
	server, err := NewServer(service, "/api", nil)
	if err != nil {
		t.Fatalf("NewServer() error = %v", err)
	}

	testServer := httptest.NewServer(server.Handler())
	defer testServer.Close()

	tests := []struct {
		name           string
		method         string
		path           string
		body           string
		wantStatusCode int
		wantContains   string
	}{
		{
			name:           "list",
			method:         http.MethodGet,
			path:           "/api/contacts",
			wantStatusCode: http.StatusOK,
			wantContains:   "\"first_name\":\"Ada\"",
		},
		{
			name:           "load",
			method:         http.MethodGet,
			path:           "/api/contacts/1",
			wantStatusCode: http.StatusOK,
			wantContains:   "\"id\":\"1\"",
		},
		{
			name:           "create",
			method:         http.MethodPost,
			path:           "/api/contacts",
			body:           `{"first_name":"Grace","last_name":"Hopper","phone_number":"456"}`,
			wantStatusCode: http.StatusCreated,
			wantContains:   "\"id\":\"created\"",
		},
		{
			name:           "update",
			method:         http.MethodPut,
			path:           "/api/contacts/1",
			body:           `{"first_name":"Grace","last_name":"Hopper","phone_number":"456"}`,
			wantStatusCode: http.StatusOK,
			wantContains:   "\"first_name\":\"Grace\"",
		},
		{
			name:           "delete",
			method:         http.MethodDelete,
			path:           "/api/contacts/1",
			wantStatusCode: http.StatusNoContent,
		},
	}

	for _, test := range tests {
		t.Run(test.name, func(t *testing.T) {
			request, err := http.NewRequest(test.method, testServer.URL+test.path, strings.NewReader(test.body))
			if err != nil {
				t.Fatalf("NewRequest() error = %v", err)
			}
			if test.body != "" {
				request.Header.Set("Content-Type", "application/json")
			}

			response, err := http.DefaultClient.Do(request)
			if err != nil {
				t.Fatalf("Do() error = %v", err)
			}
			defer response.Body.Close()

			if response.StatusCode != test.wantStatusCode {
				t.Fatalf("StatusCode = %d, want %d", response.StatusCode, test.wantStatusCode)
			}

			if test.wantContains != "" {
				body, err := io.ReadAll(response.Body)
				if err != nil {
					t.Fatalf("ReadAll() error = %v", err)
				}
				if !strings.Contains(string(body), test.wantContains) {
					t.Fatalf("response body = %s, want to contain %s", string(body), test.wantContains)
				}
			}
		})
	}
}

func TestServerRejectsBlankPrefix(t *testing.T) {
	if _, err := NewServer(application.NewService(fakeRepository{}), " ", nil); err == nil {
		t.Fatal("NewServer() error = nil, want error")
	}
}

func TestServerExposesHealthEndpoints(t *testing.T) {
	service := application.NewService(fakeRepository{})
	server, err := NewServer(service, "/api", nil)
	if err != nil {
		t.Fatalf("NewServer() error = %v", err)
	}

	testServer := httptest.NewServer(server.Handler())
	defer testServer.Close()

	tests := []struct {
		name           string
		path           string
		wantStatusCode int
		wantContains   string
	}{
		{
			name:           "live",
			path:           "/health/live",
			wantStatusCode: http.StatusOK,
			wantContains:   "\"status\":\"alive\"",
		},
		{
			name:           "ready",
			path:           "/health/ready",
			wantStatusCode: http.StatusOK,
			wantContains:   "\"status\":\"ready\"",
		},
	}

	for _, test := range tests {
		t.Run(test.name, func(t *testing.T) {
			response, err := http.Get(testServer.URL + test.path)
			if err != nil {
				t.Fatalf("Get() error = %v", err)
			}
			defer response.Body.Close()

			if response.StatusCode != test.wantStatusCode {
				t.Fatalf("StatusCode = %d, want %d", response.StatusCode, test.wantStatusCode)
			}

			body, err := io.ReadAll(response.Body)
			if err != nil {
				t.Fatalf("ReadAll() error = %v", err)
			}
			if !strings.Contains(string(body), test.wantContains) {
				t.Fatalf("response body = %s, want to contain %s", string(body), test.wantContains)
			}
		})
	}
}

type failingReadyRepository struct {
	fakeRepository
}

func (f failingReadyRepository) Ready(ctx context.Context) error {
	return errReadinessFailure
}

var errReadinessFailure = errors.New("contacts-api is unavailable")

func TestServerReturnsUnavailableWhenReadyCheckFails(t *testing.T) {
	service := application.NewService(failingReadyRepository{})
	server, err := NewServer(service, "/api", nil)
	if err != nil {
		t.Fatalf("NewServer() error = %v", err)
	}

	testServer := httptest.NewServer(server.Handler())
	defer testServer.Close()

	response, err := http.Get(testServer.URL + "/health/ready")
	if err != nil {
		t.Fatalf("Get() error = %v", err)
	}
	defer response.Body.Close()

	if response.StatusCode != http.StatusServiceUnavailable {
		t.Fatalf("StatusCode = %d, want %d", response.StatusCode, http.StatusServiceUnavailable)
	}
}
