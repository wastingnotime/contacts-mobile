# Android Contacts Search Viewport Tracks Filtered Results

- pack: android_compose_client
- runtime_targets:
  - android/app
  - external-api/axiom-exp-contacts
- architecture_mode: layered_android_client

## Discovery Scope

Refine the contacts list interaction so search and list position work together predictably:

- keep the current list, detail, create, edit, delete, sorting, search-summary, stale-data, and viewport continuity flows intact
- preserve the visible list neighborhood when the search query changes inside the currently loaded contacts
- keep the viewport behavior local and deterministic
- avoid any backend endpoint or transport change

This slice makes the viewport rule explicit for query changes that alter the filtered result set.
The intended behavior is to preserve the active visible neighborhood when possible, not to reset the list to the top on every search edit.

## Use-Case Contract

The app should continue to expose the existing local search and presentation use cases:

- `FilterContacts`
- `SortContacts`
- `LoadContacts`
- `RefreshContacts`

The UI should preserve the user’s place inside the filtered result set when the active query changes.

## Main Business Rules

- if the active query changes but the current anchor contact is still visible, keep that contact near the same position
- if the active query changes and the anchor contact disappears, keep the viewport near the closest surviving filtered row when possible
- clearing the query should still restore the full sorted list
- search filtering and list sorting should continue to determine which rows are visible
- the backend contract should remain unchanged
- the viewport rule should remain consistent with the list-scroll continuity rules so search edits do not introduce a separate reset policy

## Required Ports

- no new backend port is required
- the existing presentation state and list UI composition are the main integration points

## Initial Test Plan

- verify the viewport remains stable when the search query narrows but still includes the anchor contact
- verify the viewport falls back to the nearest surviving filtered row when the anchor is filtered out
- verify clearing the query restores the full sorted list without losing the list neighborhood
- verify search-summary, filtered-empty, stale-data, CRUD, and navigation flows remain reachable
- verify the backend contract remains unchanged

## Scenario Definition

Given a loaded contacts list, when the user changes the search query, the Android app should keep the user near the same visible neighborhood inside the filtered results instead of jumping back to the top whenever possible.

## Done Criteria

- the app module compiles in the Android Gradle project shape
- the viewport rule is explicit for search-driven filter changes
- deterministic tests cover filtered-result viewport continuity and fallback
- the existing search, sort, stale-data, CRUD, and navigation flows remain intact
- the backend contract remains unchanged
