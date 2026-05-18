# Android Contacts Request Claims Headers Impact Analysis

## Slice Intent

Keep the current Android contacts list and detail flows intact while making the backend request claims boundary explicit and configurable.

The backend already expects claims-style headers, and the current Android client has been reaching the API without that request boundary spelled out. This slice closes that gap without adding login UX.

## Why This Slice Now

The list and detail flows are already implemented. The next remaining pressure is the transport request contract itself:

- the backend runtime is claims-based
- the Android client should not silently assume anonymous access
- local development should still be deterministic and low-friction

This slice resolves that by making request claims a first-class config-backed input to the contacts BFF client.

## Impacted Boundaries

### Interfaces

- `MainActivity` must resolve and pass request claims configuration into the client composition

### Infrastructure

- `HttpContactsBffClient` must apply `x-auth-subject` and `x-auth-roles` to every request
- the HTTP client should remain focused on transport, not on login/session logic

### Configuration

- build-time defaults should provide admin-shaped claims for local use
- invalid blank claims configuration should fail early

### Tests

- add deterministic coverage for request header application
- add config-resolution tests for claims defaults and validation

## Model Pressure

The current model hypothesis still leaves auth headers unresolved. This slice chooses the narrowest viable path:

- explicit request claims
- config-backed defaults
- no UI login system
- no token refresh or session management

That keeps the app aligned with the backend while staying inside the current contacts-client scope.

## Build Guidance

The build should:

- reuse the current API client shape
- add header injection through a small configuration seam
- avoid introducing a navigation or auth framework
- preserve the deterministic tests already in place for list and detail behavior
