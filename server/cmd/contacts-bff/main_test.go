package main

import (
	"net/http"
	"net/http/httptest"
	"testing"
)

func TestHealthcheckURLUsesConfiguredListenPort(t *testing.T) {
	t.Setenv("CONTACTS_BFF_LISTEN_ADDR", "0.0.0.0:9090")

	if got, want := healthcheckURL(), "http://127.0.0.1:9090/health/ready"; got != want {
		t.Fatalf("healthcheckURL() = %q, want %q", got, want)
	}
}

func TestHealthcheckURLDefaultsTo8080(t *testing.T) {
	t.Setenv("CONTACTS_BFF_LISTEN_ADDR", "")

	if got, want := healthcheckURL(), "http://127.0.0.1:8080/health/ready"; got != want {
		t.Fatalf("healthcheckURL() = %q, want %q", got, want)
	}
}

func TestRunHealthcheckSucceedsWhenReadinessRespondsOK(t *testing.T) {
	server := httptest.NewServer(http.HandlerFunc(func(responseWriter http.ResponseWriter, request *http.Request) {
		if request.URL.Path != "/health/ready" {
			t.Fatalf("request path = %q, want /health/ready", request.URL.Path)
		}

		responseWriter.WriteHeader(http.StatusOK)
	}))
	t.Cleanup(server.Close)

	t.Setenv("CONTACTS_BFF_LISTEN_ADDR", server.Listener.Addr().String())

	if err := runHealthcheck(); err != nil {
		t.Fatalf("runHealthcheck() error = %v", err)
	}
}

func TestRunHealthcheckFailsWhenReadinessRespondsUnavailable(t *testing.T) {
	server := httptest.NewServer(http.HandlerFunc(func(responseWriter http.ResponseWriter, request *http.Request) {
		responseWriter.WriteHeader(http.StatusServiceUnavailable)
	}))
	t.Cleanup(server.Close)

	t.Setenv("CONTACTS_BFF_LISTEN_ADDR", server.Listener.Addr().String())

	if err := runHealthcheck(); err == nil {
		t.Fatal("runHealthcheck() error = nil, want non-nil")
	}
}
