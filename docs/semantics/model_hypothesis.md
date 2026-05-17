# Model Hypothesis

## Purpose

This document captures the current model hypothesis for the target domain.

Use it during `extract`, `refine`, and `build` to define vocabulary, boundaries, use cases, and slice candidates.

---

## Current Hypothesis

The repository is a native Android client for the contacts product. Its current behavior is a contacts list experience with API-backed contact detail, create, edit, and delete flows. The app consumes the external `axiom-exp-contacts` system through a Go BFF, and keeps several pieces of presentation state local.

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
- `ContactsBffClient`: the infrastructure client that performs HTTP requests to the Go BFF
- `ContactsBffBootstrap`: the interface-layer assembly that wires BFF base URL, API surface, auth headers, and client creation
- `ContactsBootstrapConfigurationResolver`: the interface-layer resolver that maps BuildConfig values into bootstrap configuration
- `ContactsBootstrapBuildConfigurationSource`: the interface-layer source that reads raw BuildConfig values for app startup
- `ContactsBootstrapDependencies`: the interface-layer value object that groups the resolved BFF client and repository dependencies
- `ContactsBffUseCaseAssembly`: the interface-layer assembly that turns the resolved BFF repository into the app use cases
- `ContactsBffViewModelFactoryAssembly`: the interface-layer assembly that turns the app use cases into the contacts view-model factory
- `ContactsBootstrap`: the final interface-layer bootstrap object that exposes the view-model factory to the activity
- `ContactsAppStart`: the interface-layer startup facade that returns the final bootstrap for the activity
- `ContactsAppBootstrapVerb`: the startup verb that exposes the app bootstrap path through a single `bootstrap()` entry point
- `LoadContactById`: the use case for loading one contact from the backend
- request claims headers: explicit claims-style headers sent with every contacts BFF request
- `ContactsBffApiSurface`: the client-facing `/api` path prefix used by the BFF contract

### Key Value Objects

- contact identifiers
- first and last names
- phone numbers
- BFF base URL configuration
- BFF API surface configuration
- BFF bootstrap configuration
- app bootstrap configuration
- app bootstrap build configuration source
- app bootstrap dependencies
- BFF use-case assembly
- BFF view-model factory assembly
- app final bootstrap object
- app start facade
- app bootstrap verb
- bootstrap configuration resolution

### Major State Transitions

- loading contacts from the backend
- rendering an empty list when the backend returns no contacts
- rendering a populated list when the backend returns contacts
- sorting the loaded contacts into a predictable local order
- filtering the loaded contacts locally by a user-entered query
- surfacing a local search summary and clear action whenever the query is active
- preserving the active search query while navigating between list, detail, and form surfaces
- preserving the active visible neighborhood when the search query changes, as long as a surviving anchor remains available
- preserving the visible list viewport while navigating between list, detail, and form surfaces
- making stale preserved data explicit after a transient refresh or reload failure
- dismissing a stale-data indicator after the user has acknowledged the warning, independently for list and detail surfaces
- moving from error to retry and back to loading
- forwarding list and detail requests through the BFF boundary before they reach `contacts-api`
- keeping the client on the fixed BFF `/api` surface instead of letting each use case assemble its own transport path
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
