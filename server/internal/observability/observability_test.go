package observability

import (
	"io"
	"net/http"
	"net/http/httptest"
	"testing"
)

func TestNewFromEnvExportsLogsToOTLP(t *testing.T) {
	received := make(chan struct{}, 1)
	requestErr := make(chan error, 1)
	collector := httptest.NewServer(http.HandlerFunc(func(responseWriter http.ResponseWriter, request *http.Request) {
		if request.URL.Path != "/v1/logs" {
			requestErr <- &unexpectedPathError{got: request.URL.Path, want: "/v1/logs"}
			responseWriter.WriteHeader(http.StatusBadRequest)
			return
		}

		body, err := io.ReadAll(request.Body)
		if err != nil {
			requestErr <- err
			responseWriter.WriteHeader(http.StatusInternalServerError)
			return
		}
		if len(body) == 0 {
			requestErr <- io.ErrUnexpectedEOF
			responseWriter.WriteHeader(http.StatusBadRequest)
			return
		}

		select {
		case received <- struct{}{}:
		default:
		}

		responseWriter.WriteHeader(http.StatusAccepted)
	}))
	defer collector.Close()

	t.Setenv(otelEndpointEnv, collector.URL)
	t.Setenv(otelServiceNameEnv, defaultServiceName)

	runtime, err := NewFromEnv()
	if err != nil {
		t.Fatalf("NewFromEnv() error = %v", err)
	}

	runtime.Logger().Info("contacts-mobile request completed", "method", "GET", "path", "/api/contacts")

	if err := runtime.Shutdown(); err != nil {
		t.Fatalf("Shutdown() error = %v", err)
	}

	select {
	case <-received:
	default:
		t.Fatal("expected OTLP logs export request")
	}

	select {
	case err := <-requestErr:
		t.Fatalf("collector request error = %v", err)
	default:
	}
}

type unexpectedPathError struct {
	got  string
	want string
}

func (e *unexpectedPathError) Error() string {
	return "request.URL.Path = " + e.got + ", want " + e.want
}
