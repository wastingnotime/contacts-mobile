package observability

import (
	"context"
	"fmt"
	"io"
	"log/slog"
	"net/url"
	"os"
	"strings"

	"go.opentelemetry.io/contrib/bridges/otelslog"
	"go.opentelemetry.io/otel/attribute"
	"go.opentelemetry.io/otel/exporters/otlp/otlplog/otlploghttp"
	sdklog "go.opentelemetry.io/otel/sdk/log"
	sdkresource "go.opentelemetry.io/otel/sdk/resource"
)

const (
	defaultServiceName    = "contacts-mobile-bff"
	defaultServiceVersion = "unknown"
	defaultLogsPath       = "/v1/logs"
	otelEndpointEnv       = "OTEL_EXPORTER_OTLP_ENDPOINT"
	otelLogsEndpointEnv   = "OTEL_EXPORTER_OTLP_LOGS_ENDPOINT"
	otelServiceNameEnv    = "OTEL_SERVICE_NAME"
	otelServiceVersionEnv = "OTEL_SERVICE_VERSION"
)

var noopLogger = slog.New(slog.NewTextHandler(io.Discard, nil))

type Runtime struct {
	logger   *slog.Logger
	shutdown func() error
}

func NewFromEnv() (*Runtime, error) {
	sourceEnv := otelLogsEndpointEnv
	logsEndpoint := strings.TrimSpace(os.Getenv(otelLogsEndpointEnv))
	if logsEndpoint == "" {
		sourceEnv = otelEndpointEnv
		logsEndpoint = strings.TrimSpace(os.Getenv(otelEndpointEnv))
	}
	if logsEndpoint == "" {
		return &Runtime{logger: noopLogger}, nil
	}

	parsed, err := url.Parse(logsEndpoint)
	if err != nil {
		return nil, fmt.Errorf("invalid %s: %w", sourceEnv, err)
	}

	endpoint := parsed.Host
	if endpoint == "" {
		endpoint = parsed.Path
	}
	if endpoint == "" {
		return nil, fmt.Errorf("invalid %s: missing host", sourceEnv)
	}

	logsPath := parsed.Path
	if logsPath == "" || logsPath == "/" {
		logsPath = defaultLogsPath
	}

	serviceName := strings.TrimSpace(os.Getenv(otelServiceNameEnv))
	if serviceName == "" {
		serviceName = defaultServiceName
	}
	serviceVersion := strings.TrimSpace(os.Getenv(otelServiceVersionEnv))
	if serviceVersion == "" {
		serviceVersion = defaultServiceVersion
	}

	resource, err := sdkresource.New(context.Background(),
		sdkresource.WithFromEnv(),
		sdkresource.WithAttributes(
			attribute.String("service.name", serviceName),
			attribute.String("service.version", serviceVersion),
			attribute.String("service.namespace", "contacts-mobile"),
		),
	)
	if err != nil {
		return nil, fmt.Errorf("create telemetry resource: %w", err)
	}

	exporterOptions := []otlploghttp.Option{
		otlploghttp.WithEndpoint(endpoint),
		otlploghttp.WithURLPath(logsPath),
	}
	if parsed.Scheme != "https" {
		exporterOptions = append(exporterOptions, otlploghttp.WithInsecure())
	}

	exporter, err := otlploghttp.New(context.Background(), exporterOptions...)
	if err != nil {
		return nil, fmt.Errorf("create otlp log exporter: %w", err)
	}

	provider := sdklog.NewLoggerProvider(
		sdklog.WithResource(resource),
		sdklog.WithProcessor(sdklog.NewBatchProcessor(exporter)),
	)

	return &Runtime{
		logger: otelslog.NewLogger(
			serviceName,
			otelslog.WithLoggerProvider(provider),
		),
		shutdown: func() error {
			return provider.Shutdown(context.Background())
		},
	}, nil
}

func (r *Runtime) Logger() *slog.Logger {
	if r == nil || r.logger == nil {
		return noopLogger
	}

	return r.logger
}

func (r *Runtime) Shutdown() error {
	if r == nil || r.shutdown == nil {
		return nil
	}

	return r.shutdown()
}
