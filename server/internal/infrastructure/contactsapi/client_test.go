package contactsapi

import (
	"context"
	"encoding/json"
	"io"
	"net/http"
	"net/http/httptest"
	"testing"

	"github.com/wastingnotime/contacts-mobile/server/internal/domain"
)

func TestClientRoutesContactsRequestsThroughTheConfiguredSurface(t *testing.T) {
	t.Run("list", func(t *testing.T) {
		server := httptest.NewServer(http.HandlerFunc(func(responseWriter http.ResponseWriter, request *http.Request) {
			assertRequest(t, request, http.MethodGet, "/api/contacts")
			writeJSON(t, responseWriter, []contactDocument{{
				ID:          "1",
				FirstName:   "Ada",
				LastName:    "Lovelace",
				PhoneNumber: "123",
			}})
		}))
		defer server.Close()

		client, err := NewClient(server.URL, "/api", "subject", "admin")
		if err != nil {
			t.Fatalf("NewClient() error = %v", err)
		}

		contacts, err := client.ListContacts(context.Background())
		if err != nil {
			t.Fatalf("ListContacts() error = %v", err)
		}
		if len(contacts) != 1 || contacts[0].ID != "1" {
			t.Fatalf("ListContacts() = %#v", contacts)
		}
	})

	t.Run("load", func(t *testing.T) {
		server := httptest.NewServer(http.HandlerFunc(func(responseWriter http.ResponseWriter, request *http.Request) {
			assertRequest(t, request, http.MethodGet, "/api/contacts/1")
			writeJSON(t, responseWriter, contactDocument{
				ID:          "1",
				FirstName:   "Ada",
				LastName:    "Lovelace",
				PhoneNumber: "123",
			})
		}))
		defer server.Close()

		client, err := NewClient(server.URL, "/api", "subject", "admin")
		if err != nil {
			t.Fatalf("NewClient() error = %v", err)
		}

		contact, err := client.LoadContactByID(context.Background(), "1")
		if err != nil {
			t.Fatalf("LoadContactByID() error = %v", err)
		}
		if contact.ID != "1" {
			t.Fatalf("LoadContactByID() = %#v", contact)
		}
	})

	t.Run("create", func(t *testing.T) {
		server := httptest.NewServer(http.HandlerFunc(func(responseWriter http.ResponseWriter, request *http.Request) {
			assertRequest(t, request, http.MethodPost, "/api/contacts")
			assertJSONBody(t, request.Body, map[string]any{
				"first_name":   "Ada",
				"last_name":    "Lovelace",
				"phone_number": "123",
			})
			writeJSON(t, responseWriter, contactDocument{
				ID:          "created",
				FirstName:   "Ada",
				LastName:    "Lovelace",
				PhoneNumber: "123",
			})
		}))
		defer server.Close()

		client, err := NewClient(server.URL, "/api", "subject", "admin")
		if err != nil {
			t.Fatalf("NewClient() error = %v", err)
		}

		contact, err := client.CreateContact(context.Background(), domain.ContactDraft{
			FirstName:   "Ada",
			LastName:    "Lovelace",
			PhoneNumber: "123",
		})
		if err != nil {
			t.Fatalf("CreateContact() error = %v", err)
		}
		if contact.ID != "created" {
			t.Fatalf("CreateContact() = %#v", contact)
		}
	})

	t.Run("update", func(t *testing.T) {
		server := httptest.NewServer(http.HandlerFunc(func(responseWriter http.ResponseWriter, request *http.Request) {
			assertRequest(t, request, http.MethodPut, "/api/contacts/1")
			assertJSONBody(t, request.Body, map[string]any{
				"first_name":   "Grace",
				"last_name":    "Hopper",
				"phone_number": "456",
			})
			writeJSON(t, responseWriter, contactDocument{
				ID:          "1",
				FirstName:   "Grace",
				LastName:    "Hopper",
				PhoneNumber: "456",
			})
		}))
		defer server.Close()

		client, err := NewClient(server.URL, "/api", "subject", "admin")
		if err != nil {
			t.Fatalf("NewClient() error = %v", err)
		}

		contact, err := client.UpdateContact(context.Background(), "1", domain.ContactDraft{
			FirstName:   "Grace",
			LastName:    "Hopper",
			PhoneNumber: "456",
		})
		if err != nil {
			t.Fatalf("UpdateContact() error = %v", err)
		}
		if contact.FirstName != "Grace" {
			t.Fatalf("UpdateContact() = %#v", contact)
		}
	})

	t.Run("delete", func(t *testing.T) {
		server := httptest.NewServer(http.HandlerFunc(func(responseWriter http.ResponseWriter, request *http.Request) {
			assertRequest(t, request, http.MethodDelete, "/api/contacts/1")
			responseWriter.WriteHeader(http.StatusNoContent)
		}))
		defer server.Close()

		client, err := NewClient(server.URL, "/api", "subject", "admin")
		if err != nil {
			t.Fatalf("NewClient() error = %v", err)
		}

		if err := client.DeleteContact(context.Background(), "1"); err != nil {
			t.Fatalf("DeleteContact() error = %v", err)
		}
	})
}

func assertRequest(t *testing.T, request *http.Request, method, path string) {
	t.Helper()

	if request.Method != method {
		t.Fatalf("request.Method = %q, want %q", request.Method, method)
	}
	if request.URL.Path != path {
		t.Fatalf("request.URL.Path = %q, want %q", request.URL.Path, path)
	}
	if request.Header.Get("x-auth-subject") != "subject" {
		t.Fatalf("x-auth-subject = %q", request.Header.Get("x-auth-subject"))
	}
	if request.Header.Get("x-auth-roles") != "admin" {
		t.Fatalf("x-auth-roles = %q", request.Header.Get("x-auth-roles"))
	}
}

func assertJSONBody(t *testing.T, body io.ReadCloser, want map[string]any) {
	t.Helper()

	defer body.Close()
	var got map[string]any
	if err := json.NewDecoder(body).Decode(&got); err != nil {
		t.Fatalf("Decode() error = %v", err)
	}
	if len(got) != len(want) {
		t.Fatalf("json body = %#v, want %#v", got, want)
	}
	for key, value := range want {
		if got[key] != value {
			t.Fatalf("json body[%q] = %#v, want %#v", key, got[key], value)
		}
	}
}

func writeJSON(t *testing.T, responseWriter http.ResponseWriter, payload any) {
	t.Helper()

	responseWriter.Header().Set("Content-Type", "application/json")
	if err := json.NewEncoder(responseWriter).Encode(payload); err != nil {
		t.Fatalf("Encode() error = %v", err)
	}
}
