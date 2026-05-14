# Android Contacts List Scroll Position Persists Across Navigation

- pack: android_compose_client
- runtime_targets:
  - android/app
  - external-api/axiom-exp-contacts
- architecture_mode: layered_android_client

## Discovery Scope

Refine the contacts list interaction so the user keeps their place when moving between the list and contact-oriented surfaces:

- keep the current list, detail, create, edit, delete, sorting, filtering, search-summary, and stale-data flows intact
- preserve the visible list viewport when navigating from the contacts list into detail or a form and back
- keep the scroll behavior local and deterministic
- avoid any backend endpoint or transport change

This slice makes the list feel continuous during navigation. It does not change the data shown in the list; it preserves where the user was looking.

## Use-Case Contract

The app should continue to expose the existing list and presentation use cases:

- `LoadContacts`
- `RefreshContacts`
- `RetryLoadContacts`
- `FilterContacts`
- `SortContacts`

The UI should retain the current list viewport while moving between list-oriented and contact-oriented screens.

## Main Business Rules

- the contacts list should remember its scroll position when the user opens a contact and returns
- the contacts list should remember its scroll position when the user opens and closes create or edit forms
- search filtering and list sorting should continue to determine which rows are visible
- clearing search or changing the visible result set should still produce deterministic list content
- the backend contract should remain unchanged

## Required Ports

- no new backend port is required
- the existing presentation state and list UI composition are the main integration points

## Initial Test Plan

- verify the list viewport survives opening and closing contact detail
- verify the list viewport survives opening and closing create and edit forms
- verify the viewport is restored after returning to the contacts list
- verify search, sort, filtered-empty, stale-data, and CRUD flows remain reachable
- verify the backend contract remains unchanged

## Scenario Definition

Given a loaded contacts list, when the user scrolls down, opens a contact or form, and then returns, the Android app should preserve the visible portion of the contacts list instead of snapping back to the top.

## Done Criteria

- the app module compiles in the Android Gradle project shape
- the visible list viewport survives navigation to detail and forms
- deterministic tests cover viewport persistence and restoration
- the existing search, sort, stale-data, and CRUD flows remain intact
- the backend contract remains unchanged
