# Runtime Contract

## Scope

This contract describes the repository-owned BFF runtime's operational health surface.

## Stable Semantics

- `GET /health/live` reports process liveness only
- `GET /health/ready` reports readiness for production traffic
- readiness must account for internal initialization and the downstream `contacts-api` dependency that the BFF truly needs
- health endpoints are operational probes, not user-facing app behavior
- readiness should fail closed when the BFF cannot satisfy its runtime dependency contract

## Notes

The liveness endpoint should stay cheap and dependency-free. The readiness endpoint should stay honest about upstream failure instead of masking it behind a successful HTTP status.
