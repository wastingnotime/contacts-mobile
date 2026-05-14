# Android Contacts List Viewport Survives List Content Changes

- pack: android_compose_client
- runtime_targets:
  - android/app
  - external-api/axiom-exp-contacts
- architecture_mode: layered_android_client

## Discovery Scope

Refine the contacts list interaction so the user does not lose their place when the list contents change:

- keep the current list, detail, create, edit, delete, sorting, filtering, search-summary, stale-data, and viewport continuity flows intact
- preserve the visible list viewport when contacts are created, updated, deleted, or refreshed
- keep the viewport behavior local and deterministic
- avoid any backend endpoint or transport change

This slice extends the existing viewport continuity work from navigation-only behavior to actual list content changes.

## Use-Case Contract

The app should continue to expose the existing list and CRUD use cases:

- `LoadContacts`
- `RefreshContacts`
- `RetryLoadContacts`
- `CreateContact`
- `UpdateContact`
- `DeleteContact`
- `FilterContacts`
- `SortContacts`

The UI should retain the current list viewport while the contacts collection changes.

## Main Business Rules

- creating a contact should not abruptly reset the visible list position
- updating a contact should not abruptly reset the visible list position
- deleting a contact should not abruptly reset the visible list position
- refreshing the list should keep the current viewport stable when possible
- search filtering and list sorting should continue to determine which rows are visible
- the backend contract should remain unchanged

## Required Ports

- no new backend port is required
- the existing presentation state and list UI composition are the main integration points

## Initial Test Plan

- verify the viewport remains stable after creating a contact from the list flow
- verify the viewport remains stable after updating a contact from the detail flow
- verify the viewport remains stable after deleting a contact from the detail flow
- verify the viewport remains stable after a successful refresh
- verify search, sort, filtered-empty, stale-data, and CRUD flows remain reachable
- verify the backend contract remains unchanged

## Scenario Definition

Given a loaded contacts list that is scrolled away from the top, when the user creates, edits, deletes, or refreshes contacts, the Android app should keep the visible portion of the list stable instead of snapping back to the top.

## Done Criteria

- the app module compiles in the Android Gradle project shape
- the visible list viewport survives list content changes
- deterministic tests cover viewport behavior across CRUD and refresh transitions
- the existing search, sort, stale-data, and navigation flows remain intact
- the backend contract remains unchanged
