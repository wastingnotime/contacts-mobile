package main

import (
	"net/http"
	"net/http/httptest"
	"testing"
)

func TestHealthcheckURLUsesConfiguredListenPort(t *testing.T) {
	t.Setenv("CONTACTS_BFF_LISTEN_ADDR", "0.0.0.0:9090")

	if got, want := healthcheckURL("/health/ready"), "http://127.0.0.1:9090/health/ready"; got != want {
		t.Fatalf("healthcheckURL() = %q, want %q", got, want)
	}
}

func TestHealthcheckURLDefaultsTo8080(t *testing.T) {
	t.Setenv("CONTACTS_BFF_LISTEN_ADDR", "")

	if got, want := healthcheckURL("/health/live"), "http://127.0.0.1:8080/health/live"; got != want {
		t.Fatalf("healthcheckURL() = %q, want %q", got, want)
	}
}

func TestRunHealthcheckSucceedsWhenBothHealthEndpointsRespondOK(t *testing.T) {
	requestedPaths := make([]string, 0, 2)
	server := httptest.NewServer(http.HandlerFunc(func(responseWriter http.ResponseWriter, request *http.Request) {
		requestedPaths = append(requestedPaths, request.URL.Path)
		switch request.URL.Path {
		case "/health/live", "/health/ready":
			responseWriter.WriteHeader(http.StatusOK)
		default:
			t.Fatalf("request path = %q, want /health/live or /health/ready", request.URL.Path)
		}
	}))
	t.Cleanup(server.Close)

	t.Setenv("CONTACTS_BFF_LISTEN_ADDR", server.Listener.Addr().String())

	if err := runHealthcheck("/health/live", "/health/ready"); err != nil {
		t.Fatalf("runHealthcheck() error = %v", err)
	}

	if got, want := len(requestedPaths), 2; got != want {
		t.Fatalf("requested paths = %v, want %d requests", requestedPaths, want)
	}
}

func TestRunHealthcheckFailsWhenLivenessRespondsUnavailable(t *testing.T) {
	server := httptest.NewServer(http.HandlerFunc(func(responseWriter http.ResponseWriter, request *http.Request) {
		switch request.URL.Path {
		case "/health/live":
			responseWriter.WriteHeader(http.StatusServiceUnavailable)
		case "/health/ready":
			responseWriter.WriteHeader(http.StatusOK)
		default:
			t.Fatalf("request path = %q, want /health/live or /health/ready", request.URL.Path)
		}
	}))
	t.Cleanup(server.Close)

	t.Setenv("CONTACTS_BFF_LISTEN_ADDR", server.Listener.Addr().String())

	if err := runHealthcheck("/health/live", "/health/ready"); err == nil {
		t.Fatal("runHealthcheck() error = nil, want non-nil")
	}
}

func TestRunHealthcheckFailsWhenReadinessRespondsUnavailable(t *testing.T) {
	server := httptest.NewServer(http.HandlerFunc(func(responseWriter http.ResponseWriter, request *http.Request) {
		switch request.URL.Path {
		case "/health/live":
			responseWriter.WriteHeader(http.StatusOK)
		case "/health/ready":
			responseWriter.WriteHeader(http.StatusServiceUnavailable)
		default:
			t.Fatalf("request path = %q, want /health/live or /health/ready", request.URL.Path)
		}
	}))
	t.Cleanup(server.Close)

	t.Setenv("CONTACTS_BFF_LISTEN_ADDR", server.Listener.Addr().String())

	if err := runHealthcheck("/health/live", "/health/ready"); err == nil {
		t.Fatal("runHealthcheck() error = nil, want non-nil")
	}
}
