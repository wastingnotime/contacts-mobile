# Contacts BFF Runtime

This directory contains the repository-owned Go BFF runtime for Contacts Mobile.

The scaffold is intentionally small:

- `cmd/contacts-bff/` contains the HTTP server entrypoint
- `internal/config/` resolves runtime configuration
- `internal/domain/` holds the BFF-facing contact model
- `internal/application/` coordinates repository operations
- `internal/infrastructure/contactsapi/` talks to the downstream `contacts-api`
- `internal/transport/http/` exposes the client-facing `/api` routes

The Android app remains in `app/`; this tree owns the repository-owned Go runtime boundary.

## Local Run

From the repository root:

```bash
cd server
CONTACTS_API_BASE_URL=http://127.0.0.1:8081 go run ./cmd/contacts-bff
```

The BFF listens on `:8080` by default.

Required environment:

- `CONTACTS_API_BASE_URL`

Optional environment:

- `CONTACTS_BFF_LISTEN_ADDR`
- `CONTACTS_BFF_API_PREFIX`
- `CONTACTS_BFF_AUTH_SUBJECT`
- `CONTACTS_BFF_AUTH_ROLES`
- `OTEL_EXPORTER_OTLP_ENDPOINT`
- `OTEL_EXPORTER_OTLP_LOGS_ENDPOINT`
- `OTEL_SERVICE_NAME`
- `OTEL_SERVICE_VERSION`

## Health

- `GET /health/live` reports process liveness only
- `GET /health/ready` reports whether the BFF can serve production traffic and reach the downstream `contacts-api`
- the release image uses `/health/live` and `/health/ready` through the `contacts-bff healthcheck /health/live /health/ready` command for Docker `HEALTHCHECK` metadata
