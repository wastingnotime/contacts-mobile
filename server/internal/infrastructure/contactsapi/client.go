package contactsapi

import (
	"bytes"
	"context"
	"encoding/json"
	"fmt"
	"io"
	"net/http"
	"net/url"
	"path"
	"strings"
	"time"

	"github.com/wastingnotime/contacts-mobile/server/internal/domain"
)

type Client struct {
	baseURL    string
	apiPrefix  string
	subject    string
	roles      string
	httpClient *http.Client
}

func NewClient(baseURL, apiPrefix, subject, roles string) (*Client, error) {
	normalizedBaseURL := strings.TrimSpace(baseURL)
	if normalizedBaseURL == "" {
		return nil, fmt.Errorf("contactsApiBaseUrl must not be blank")
	}
	if _, err := url.ParseRequestURI(normalizedBaseURL); err != nil {
		return nil, fmt.Errorf("contactsApiBaseUrl must be a valid URL: %w", err)
	}

	normalizedPrefix := normalizePathPrefix(apiPrefix)
	if normalizedPrefix == "" {
		return nil, fmt.Errorf("contactsBffApiPrefix must not be blank")
	}

	normalizedSubject := strings.TrimSpace(subject)
	if normalizedSubject == "" {
		return nil, fmt.Errorf("contactsBffAuthSubject must not be blank")
	}

	normalizedRoles := strings.TrimSpace(roles)
	if normalizedRoles == "" {
		return nil, fmt.Errorf("contactsBffAuthRoles must not be blank")
	}

	return &Client{
		baseURL:    strings.TrimRight(normalizedBaseURL, "/"),
		apiPrefix:  normalizedPrefix,
		subject:    normalizedSubject,
		roles:      normalizedRoles,
		httpClient: &http.Client{Timeout: 10 * time.Second},
	}, nil
}

func (c *Client) ListContacts(ctx context.Context) ([]domain.Contact, error) {
	var response []contactDocument
	if err := c.doJSONRequest(ctx, http.MethodGet, c.contactsPath(), nil, &response); err != nil {
		return nil, err
	}

	contacts := make([]domain.Contact, 0, len(response))
	for _, document := range response {
		contacts = append(contacts, document.toDomain())
	}
	return contacts, nil
}

func (c *Client) LoadContactByID(ctx context.Context, id string) (domain.Contact, error) {
	var response contactDocument
	if err := c.doJSONRequest(ctx, http.MethodGet, c.contactPath(id), nil, &response); err != nil {
		return domain.Contact{}, err
	}
	return response.toDomain(), nil
}

func (c *Client) CreateContact(ctx context.Context, draft domain.ContactDraft) (domain.Contact, error) {
	request := contactDraftDocumentFromDomain(draft)
	var response contactDocument
	if err := c.doJSONRequest(ctx, http.MethodPost, c.contactsPath(), request, &response); err != nil {
		return domain.Contact{}, err
	}
	return response.toDomain(), nil
}

func (c *Client) UpdateContact(ctx context.Context, id string, draft domain.ContactDraft) (domain.Contact, error) {
	request := contactDraftDocumentFromDomain(draft)
	var response contactDocument
	if err := c.doJSONRequest(ctx, http.MethodPut, c.contactPath(id), request, &response); err != nil {
		return domain.Contact{}, err
	}
	return response.toDomain(), nil
}

func (c *Client) DeleteContact(ctx context.Context, id string) error {
	return c.doJSONRequest(ctx, http.MethodDelete, c.contactPath(id), nil, nil)
}

func (c *Client) doJSONRequest(
	ctx context.Context,
	method, requestPath string,
	body any,
	responseBody any,
) error {
	var bodyReader io.Reader
	if body != nil {
		payload, err := json.Marshal(body)
		if err != nil {
			return fmt.Errorf("encode contacts request: %w", err)
		}
		bodyReader = bytes.NewReader(payload)
	}

	request, err := http.NewRequestWithContext(ctx, method, c.baseURL+requestPath, bodyReader)
	if err != nil {
		return fmt.Errorf("build contacts request: %w", err)
	}

	request.Header.Set("Accept", "application/json")
	request.Header.Set("x-auth-subject", c.subject)
	request.Header.Set("x-auth-roles", c.roles)
	if body != nil {
		request.Header.Set("Content-Type", "application/json")
	}

	response, err := c.httpClient.Do(request)
	if err != nil {
		return fmt.Errorf("perform contacts request: %w", err)
	}
	defer response.Body.Close()

	if response.StatusCode == http.StatusNotFound {
		return domain.ErrContactNotFound
	}
	if response.StatusCode < 200 || response.StatusCode >= 300 {
		return fmt.Errorf("contacts upstream responded with status %d", response.StatusCode)
	}

	if responseBody == nil || response.StatusCode == http.StatusNoContent {
		return nil
	}

	decoder := json.NewDecoder(response.Body)
	if err := decoder.Decode(responseBody); err != nil {
		return fmt.Errorf("decode contacts response: %w", err)
	}
	return nil
}

func (c *Client) contactsPath() string {
	return joinPath(c.apiPrefix, "contacts")
}

func (c *Client) contactPath(id string) string {
	return joinPath(c.apiPrefix, "contacts", url.PathEscape(id))
}

type contactDocument struct {
	ID          string `json:"id"`
	FirstName   string `json:"first_name"`
	LastName    string `json:"last_name"`
	PhoneNumber string `json:"phone_number"`
}

type contactDraftDocument struct {
	FirstName   string `json:"first_name"`
	LastName    string `json:"last_name"`
	PhoneNumber string `json:"phone_number"`
}

func contactDraftDocumentFromDomain(draft domain.ContactDraft) contactDraftDocument {
	return contactDraftDocument{
		FirstName:   draft.FirstName,
		LastName:    draft.LastName,
		PhoneNumber: draft.PhoneNumber,
	}
}

func (document contactDocument) toDomain() domain.Contact {
	return domain.Contact{
		ID:          document.ID,
		FirstName:   document.FirstName,
		LastName:    document.LastName,
		PhoneNumber: document.PhoneNumber,
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

func joinPath(prefix string, parts ...string) string {
	segments := []string{strings.TrimRight(prefix, "/")}
	for _, part := range parts {
		segments = append(segments, strings.Trim(part, "/"))
	}
	return path.Join(segments...)
}
