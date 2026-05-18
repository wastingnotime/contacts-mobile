# Android Client Bootstrap EGD

## Slice

`docs/slices/android_contacts_list_bff_client.md`

## Evidence Reviewed

- `docs/semantics/model_hypothesis.md`
- `docs/semantics/domain_background_knowledge.md`
- `docs/slices/android_contacts_list_bff_client.md`
- `work/changes/2026-05-13-android-client-bootstrap/implementation.md`
- implementation under `app/`
- `./gradlew test`

## Summary

The implemented Android slice matches the main expectation in the slice: the app loads `GET /api/contacts`, maps the snake_case transport payload into app models, and renders loading, empty, error, and list states with deterministic tests.

The build is also aligned with the current Android 15 target policy and the base URL is configurable for emulator, local-device, and production environments.

## Findings

### 1. Core list behavior is aligned with the slice

Observed behavior:

- `LoadContacts` returns empty or loaded states from the repository
- the HTTP client targets `GET /api/contacts`
- snake_case transport fields are mapped at the infrastructure boundary
- the UI exposes loading, empty, error, and list states
- retry is available on empty and error states

Assessment:

- this satisfies the initial slice contract and the current domain background expectations
- no blocking mismatch was found between the slice intent and the implemented list flow

### 2. The contact detail screen is semantically extra for this slice

Observed behavior:

- the app now includes a local detail view reached by tapping a contact row
- the detail screen shows the selected contact from the already-loaded list
- the detail view does not load a contact from the API

Assessment:

- this is not a correctness bug for the current slice
- it is a scope expansion beyond the original list-only slice and could be mistaken for an API-backed detail capability later
- if the next slice is meant to model detail as a business capability, the contract should make the API boundary explicit rather than treating the current local selection as that boundary

## Review Questions

1. Should the next slice formalize contact detail as an API-backed use case, or keep the app list-first and treat the current detail screen as a temporary UI affordance?
2. Should base URL selection remain build-time only, or should the repo eventually expose a runtime/debug switch for emulator versus local-device use?
3. Do we want the refresh action to preserve and visibly refresh the current list state, or is replacing the screen with loading sufficient for now?

## Recommendation

Continue to the next slice.

Reasoning:

- the implemented list flow is coherent and test-backed
- the remaining issues are semantic scope questions, not release-blocking defects
- the next refinement step should decide whether detail becomes a real API-backed capability or stays out of the slice boundary
