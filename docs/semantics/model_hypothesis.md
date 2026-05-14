# Model Hypothesis

## Purpose

This document captures the current model hypothesis for the target domain.

Use it during `extract`, `refine`, and `build` to define vocabulary, boundaries, use cases, and slice candidates.

---

## Current Hypothesis

The repository is a native Android client for the contacts product. Its current behavior is a contacts list experience plus an API-backed contact detail experience that both consume the external `axiom-exp-contacts` API directly.

### Core Concepts

- `Contact`: a read-only contact record loaded from the backend
- `ContactsList`: the collection state shown on the first screen
- `ContactDetail`: the selected-contact state shown after tapping a contact
- `ContactsSearchQuery`: the local query used to narrow the loaded contacts list
- `ContactsSortOrder`: the local ordering applied to loaded contacts
- `ContactsListViewportState`: the local scroll position used to resume the visible part of the contacts list
- `ContactsLoadingState`: the view state while the API request is in flight
- `ContactsErrorState`: the view state when loading fails
- `ContactsFreshnessState`: the visible indicator for whether loaded data is fresh or stale
- `ContactsStaleAcknowledgement`: the local acknowledgement that the stale indicator has been dismissed
- `ContactsRepository`: the app port for retrieving contacts
- `ContactsApiClient`: the infrastructure client that performs HTTP requests
- `LoadContactById`: the use case for loading one contact from the backend
- request claims headers: explicit claims-style headers sent with every contacts API request

### Key Value Objects

- contact identifiers
- first and last names
- phone numbers
- base URL configuration

### Major State Transitions

- loading contacts from the backend
- rendering an empty list when the backend returns no contacts
- rendering a populated list when the backend returns contacts
- sorting the loaded contacts into a predictable local order
- filtering the loaded contacts locally by a user-entered query
- preserving the active search query while navigating between list, detail, and form surfaces
- preserving the visible list viewport while navigating between list, detail, and form surfaces
- making stale preserved data explicit after a transient refresh or reload failure
- dismissing a stale-data indicator after the user has acknowledged the warning
- moving from error to retry and back to loading
- loading a contact detail by id from the backend
- rendering an explicit not-found state when a requested contact is missing
- moving from detail error or not-found back to loading on retry
- preserving last known contacts or contact detail while a refresh or reload fails

### Candidate Use Cases

- `LoadContacts`
- `RefreshContacts`
- `RetryLoadContacts`
- `LoadContactById`
- `FilterContacts`
- `SortContacts`

### Unresolved Tensions

- whether the app should preserve last known data during transient failures or replace the view with a hard error immediately
- whether the app should preserve the current list viewport inside the active search result set when the query changes, or intentionally reset it when filtering changes the visible rows
