# Model Hypothesis

## Purpose

This document captures the current model hypothesis for the target domain.

Use it during `extract`, `refine`, and `build` to define vocabulary, boundaries, use cases, and slice candidates.

---

## Current Hypothesis

The repository is a native Android client for the contacts product. Its current behavior is a contacts list experience with API-backed contact detail, create, edit, and delete flows. The app consumes the external `axiom-exp-contacts` API directly and keeps several pieces of presentation state local.

### Core Concepts

- `Contact`: a read-only contact record loaded from the backend
- `ContactsList`: the collection state shown on the first screen
- `ContactDetail`: the selected-contact state shown after tapping a contact
- `CreateContact`: the write flow for creating a contact in the backend
- `UpdateContact`: the write flow for editing a contact in the backend
- `DeleteContact`: the write flow for deleting a contact in the backend
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
- preserving the active visible neighborhood when the search query changes, as long as a surviving anchor remains available
- preserving the visible list viewport while navigating between list, detail, and form surfaces
- making stale preserved data explicit after a transient refresh or reload failure
- dismissing a stale-data indicator after the user has acknowledged the warning
- moving from error to retry and back to loading
- loading a contact detail by id from the backend
- rendering an explicit not-found state when a requested contact is missing
- moving from detail error or not-found back to loading on retry
- creating a contact from a local form and surfacing the created contact back into the app flow
- updating a contact from a local form and reflecting the saved values back into list and detail state
- deleting a contact from the backend and returning to the list when successful
- preserving last known contacts or contact detail while a refresh or reload fails

### Candidate Use Cases

- `LoadContacts`
- `RefreshContacts`
- `RetryLoadContacts`
- `LoadContactById`
- `FilterContacts`
- `SortContacts`
- `CreateContact`
- `UpdateContact`
- `DeleteContact`
