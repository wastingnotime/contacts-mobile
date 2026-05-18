# Android Contact Detail API Client EGD

## Slice

`docs/slices/android_contacts_detail_bff_api_contract.md`

## Evidence Reviewed

- `docs/semantics/model_hypothesis.md`
- `docs/semantics/domain_background_knowledge.md`
- `docs/slices/android_contacts_detail_bff_api_contract.md`
- `work/changes/2026-05-13-android-contact-detail-api-client/impact_analysis.md`
- `work/changes/2026-05-13-android-contact-detail-api-client/implementation.md`
- implementation under `app/`
- `./gradlew test`

## Summary

The implemented slice matches the refined intent. The app now treats contact detail as a repository-owned BFF-backed capability, loads `GET /api/contacts/{id}`, maps the single-contact payload at the transport boundary, and renders a real detail state with loading, not-found, error, and loaded outcomes.

The list flow remains intact, the base URL configuration remains build-driven, and the tests cover the new repository and view-model seams deterministically.

## Findings

### 1. The detail flow is now semantically aligned with the backend contract

Observed behavior:

- tapping a contact opens a detail fetch by id
- the repository resolves a single contact from the backend rather than reusing list data only
- `GET /api/contacts/{id}` is represented explicitly in the HTTP client
- the UI distinguishes loading, loaded, not-found, and error states

Assessment:

- this resolves the earlier semantic gap from the local-only detail screen
- the implementation now matches the repository-owned BFF contract that `GET /api/contacts/{id}` exists
- no blocking mismatch remains for the detail slice itself

### 2. Refresh and back behavior are plausible, but still minimal

Observed behavior:

- refresh while detail is open reloads the active contact detail
- the detail view provides a back path and a retry path
- the implementation does not add any broader navigation structure

Assessment:

- this is acceptable for the current slice
- the behavior is intentionally narrow and deterministic
- a later slice may still need to decide whether the list should preserve scroll/selection history or whether deeper navigation deserves a navigation library

### 3. One semantic tension remains at the model level, but it is not blocking

Observed behavior:

- `docs/semantics/model_hypothesis.md` still lists contact detail as an unresolved tension
- the codebase has now resolved that tension in favor of API-backed detail

Assessment:

- this is a documentation drift, not a runtime defect
- it should be updated in the next extract/refine loop so the semantic docs stop describing detail as undecided

## Review Questions

1. Should the semantic hypothesis be updated now to mark contact detail as resolved in favor of `LoadContactById`?
2. Do we want to keep refresh limited to the active view state, or should the next slice give the list and detail flows separate reload actions?
3. Is a full Android navigation library warranted next, or is the current state-based routing still the right level of complexity?

## Recommendation

Continue to the next slice, with a documentation refresh in the next extract/refine pass.

Reasoning:

- the detail flow now matches the available backend contract
- the remaining issues are around semantic bookkeeping and future UX shape, not correctness
- the implementation is small, test-backed, and aligned with the refined slice
