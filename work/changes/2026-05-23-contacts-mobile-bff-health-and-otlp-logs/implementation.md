# Implementation: Contacts Mobile BFF health and OTLP logs

Implemented the current mobile BFF runtime follow-up in `contacts-mobile`:

- added `/health/live` for process liveness only
- added `/health/ready` for readiness against the downstream `contacts-api`
- wired readiness through the BFF service boundary so dependency checks stay explicit
- exported BFF runtime logs through OTLP when collector configuration is present
- kept the public runtime and telemetry semantics explicit in repository contract docs
- added deterministic tests for health routing, readiness failure, and OTLP log export

Validation:

- `go test ./...` in `server/`
