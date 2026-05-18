# Android Contact Detail API Client Impact Analysis

## Slice Intent

Refine the current Android client from list-only browsing into an API-backed contact detail flow.

The current UI already has a local detail view, but the next slice should make detail a real backend-backed capability instead of a list-only presentation trick.

## Why This Slice Now

The BFF already exposes `GET /api/contacts/{id}` and the current implementation has already proven the list flow. The strongest remaining semantic gap is that the app can show a selected contact locally, but it cannot yet prove that the selected contact is loadable as a standalone backend resource.

This slice closes that gap without broadening into create, edit, search, or caching.

## Impacted Boundaries

### Interfaces

- replace the current local-only detail presentation with a detail route that loads by contact id
- preserve the existing list screen and navigation into detail
- add explicit loading, not-found, and error affordances for detail

### Application

- add a detail use case such as `LoadContactById`
- keep the list use case intact
- define the repository contract for fetching one contact by id

### Infrastructure

- extend the HTTP client to call `GET /api/contacts/{id}`
- map single-contact transport payloads from snake_case into app models
- preserve the build-time base URL selection already introduced

### Tests

- add deterministic tests for single-contact transport parsing
- add use-case tests for contact lookup success, missing contact, and failure
- add UI-state tests for list-to-detail navigation and detail retry/back behavior

## Model Pressure

The current model hypothesis already contains the unresolved tension about whether detail should exist. EGD showed that the local detail screen is semantically extra, so the refinement now resolves that tension toward an API-backed detail slice.

The slice should still stay narrow:

- one selected contact
- one detail fetch
- no edit, no create, no offline cache

## Build Guidance

The build step should prefer the smallest code change that makes detail a real backend-backed behavior.

It should not:

- redesign the entire list architecture
- add a navigation library unless the current simple state model stops being sufficient
- introduce extra contact actions beyond detail loading and retry
