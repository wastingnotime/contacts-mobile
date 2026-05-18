# Android Contacts Request Claims Headers EGD

## Slice

`docs/slices/android_contacts_request_claims_headers.md`

## Evidence Reviewed

- `docs/semantics/model_hypothesis.md`
- `docs/semantics/domain_background_knowledge.md`
- `docs/slices/android_contacts_request_claims_headers.md`
- `work/changes/2026-05-13-android-contacts-request-claims-headers/impact_analysis.md`
- `work/changes/2026-05-13-android-contacts-request-claims-headers/implementation.md`
- `app/src/main/java/org/wastingnotime/contactsmobile/infrastructure/config/ContactsBffAuthConfiguration.kt`
- `app/src/main/java/org/wastingnotime/contactsmobile/infrastructure/http/HttpContactsBffClient.kt`
- `app/src/main/java/org/wastingnotime/contactsmobile/interfaces/MainActivity.kt`
- `app/src/test/java/org/wastingnotime/contactsmobile/infrastructure/config/ContactsBffAuthHeadersResolverTest.kt`
- `app/src/test/java/org/wastingnotime/contactsmobile/infrastructure/http/HttpContactsBffClientTest.kt`
- `./gradlew test`

## Summary

The implemented slice matches the refined intent. The Android client now resolves request-claims configuration at build time, composes the contacts BFF client with explicit claims headers, and applies `x-auth-subject` and `x-auth-roles` to every contacts BFF request without introducing login or session machinery.

The existing list and detail flows remain intact, and the deterministic tests cover the config seam and transport boundary.

## Findings

### 1. Request-claims behavior is explicit at the transport boundary

Observed behavior:

- `MainActivity` resolves auth claims from build config and passes them into the HTTP client composition
- `HttpContactsBffClient` applies the headers on every request path
- the tests confirm list, detail, create, update, and delete requests all carry the claims headers

Assessment:

- this satisfies the slice intent to make the request boundary explicit
- no hidden login/session layer has been introduced
- the transport contract is visible and deterministic

### 2. Configuration validation is aligned with the stated constraints

Observed behavior:

- the default local claims are admin-shaped
- blank subject or roles values fail early
- the build-time config still supports emulator, local-device, and production selection

Assessment:

- this matches the slice contract and impact analysis
- there is no blocking ambiguity about how the app acquires claims

### 3. No blocking expectation gap remains

Observed behavior:

- the list and detail flows remain otherwise unchanged
- the request claims layer does not alter UI behavior
- unit tests pass

Assessment:

- the slice is aligned with the refined model and domain background
- no release-blocking mismatch was found

## Review Questions

1. Do we want the request-claims header names to be surfaced in one shared transport contract note, or is the current config/client seam enough?
2. Should later slices expose any runtime/debug override for claims, or keep build-time selection as the only input?

## Recommendation

Continue to release.

Reasoning:

- the built behavior matches the slice intent
- the tests cover the request boundary deterministically
- the remaining questions are future-scope choices, not blocking defects
