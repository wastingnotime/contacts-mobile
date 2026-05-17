# Impact Analysis: Android Mobile BFF Boundary

## Context

The mobile client is no longer modeled as a direct consumer of `contacts-api`. The repository now treats a Go BFF as the client-facing transport boundary.

## Impacted Areas

- `docs/semantics/model_hypothesis.md`
- `docs/semantics/domain_background_knowledge.md`
- `docs/slices/android_contacts_list_api_client.md`
- `architecture.md`
- `docs/packs/android_compose_client.md`
- `decisions.md`

## Model Pressure

The main pressure is transport decoupling.

The mobile app should remain stable even if `contacts-api` changes its request or response shape. The Go BFF should absorb that drift first, even when the initial implementation is close to 1:1 forwarding.

## Next Slice Boundaries

The next build slice should implement the list flow against the BFF boundary, not against the backend directly.

That means the slice needs:

- a BFF-facing HTTP client or gateway port
- a configurable BFF base URL
- transport mapping that still understands the backend contract
- tests that prove the UI path works without coupling the app to `contacts-api`

## Constraints

- do not reintroduce direct Android-to-backend coupling
- keep the BFF contract narrow for the first slice
- keep the BFF implementation replaceable as the backend evolves
