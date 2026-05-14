# Android Contacts Search Context Persists Across Navigation

- pack: android_compose_client
- runtime_targets:
  - android/app
  - external-api/axiom-exp-contacts
- architecture_mode: layered_android_client

## Discovery Scope

Refine the local search behavior for the Android contacts client:

- keep the current list, detail, create, edit, delete, sorting, filtering, and search-summary flows intact
- preserve the active search query when navigating between list, detail, and form surfaces
- keep the filtered list visible after returning to the list from detail or forms
- keep search local and deterministic
- avoid any backend endpoint or transport change

This slice makes the search interaction feel continuous across navigation. It does not introduce new search behavior; it preserves the existing local search context while the user moves around the app.

## Use-Case Contract

The app should continue to expose the existing local filter use case:

- `FilterContacts`

The UI should retain the current search context while moving between list-oriented and contact-oriented screens.

## Main Business Rules

- search remains local and deterministic
- the current query should survive navigation to detail and back
- the current query should survive opening and closing create/edit forms
- the filtered result set should continue to use the active query when the user returns to the list
- clearing the query should still restore the full sorted list

## Required Ports

- no new backend port is required
- the existing presentation state and navigation flow are the main integration points

## Initial Test Plan

- verify the search query survives opening and closing contact detail
- verify the search query survives opening and closing create and edit forms
- verify the filtered result set remains active when returning to the list
- verify the clear-search action still restores the full list
- verify detail, create, edit, delete, sort, and filtered-empty flows remain reachable

## Scenario Definition

Given a loaded contacts list with an active query, when the user opens a contact or a form and then returns, the Android app should preserve the search query and the corresponding filtered result set.

Clearing the query should still restore the full sorted list.

## Done Criteria

- the app module compiles in the Android Gradle project shape
- the active search context survives navigation to detail and forms
- deterministic tests cover query persistence and restoration
- the existing search-summary, filtered-empty, sort, and CRUD flows remain intact
- the backend contract remains unchanged
