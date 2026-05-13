# Model Hypothesis

## Purpose

This document captures the current model hypothesis for the target domain.

Use it during `extract`, `refine`, and `build` to define vocabulary, boundaries, use cases, and slice candidates.

---

## Current Hypothesis

The repository is a native Android client for the contacts product. Its first slice is a contacts list experience that consumes the external `axiom-exp-contacts` API directly.

### Core Concepts

- `Contact`: a read-only contact record loaded from the backend
- `ContactsList`: the collection state shown on the first screen
- `ContactsLoadingState`: the view state while the API request is in flight
- `ContactsErrorState`: the view state when loading fails
- `ContactsRepository`: the app port for retrieving contacts
- `ContactsApiClient`: the infrastructure client that performs HTTP requests

### Key Value Objects

- contact identifiers
- first and last names
- phone numbers
- base URL configuration

### Major State Transitions

- loading contacts from the backend
- rendering an empty list when the backend returns no contacts
- rendering a populated list when the backend returns contacts
- moving from error to retry and back to loading

### Candidate Use Cases

- `LoadContacts`
- `RefreshContacts`
- `RetryLoadContacts`

### Unresolved Tensions

- whether the first slice should stay list-only or include contact detail next
- whether the app should carry auth headers or rely on the backend's default admin claims
- whether future slices will need offline caching or only live fetches
