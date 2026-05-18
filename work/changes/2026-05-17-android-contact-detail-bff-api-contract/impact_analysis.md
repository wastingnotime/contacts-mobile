# Android Contacts Detail BFF API Contract Impact Analysis

## Slice Intent

Keep the selected-contact detail experience intact while tightening the contract at the repository-owned BFF boundary.

The app already has API-backed detail behavior. The remaining pressure is to make the detail route and its boundary language explicit so the detail flow does not drift back toward direct backend coupling.

## Why This Slice Now

The current implementation already resolves a contact by id, but the older slice description still speaks in direct-API terms. The refined slice should align the detail story with the repository-owned Go BFF boundary and keep the selected-contact fetch contract explicit.

## Impacted Boundaries

### Interfaces

- preserve the current list-to-detail interaction
- keep loading, detail, not-found, and error states honest
- keep the detail view reachable without adding a navigation library

### Application

- keep `LoadContactById` as the single detail use case
- keep the repository contract focused on loading one contact by id

### Infrastructure

- keep the BFF client as the only transport seam for the selected-contact fetch
- keep the `/api/contacts/{id}` route explicit through the repository-owned BFF surface helper
- preserve snake_case transport mapping at the HTTP boundary

### Tests

- add or preserve deterministic tests for single-contact mapping
- add or preserve tests for detail lookup success, not-found, and failure
- add or preserve tests that the selected contact opens the repository-owned BFF-backed detail loading flow

## Model Pressure

The repository now treats contact detail as a backend-backed capability. The pressure here is not whether detail should exist, but whether the detail route remains visibly anchored to the repository-owned BFF contract instead of slipping back into backend-specific path composition.

The slice should stay narrow:

- one selected contact
- one repository-owned BFF-backed detail fetch
- no edit, create, delete, or cache redesign

## Build Guidance

The build step, if needed later, should prefer the smallest change that keeps the detail contract explicit at the repository-owned BFF boundary.

It should not:

- redesign the list architecture
- introduce a navigation library unless the state model stops being sufficient
- add extra contact actions beyond detail loading and retry
