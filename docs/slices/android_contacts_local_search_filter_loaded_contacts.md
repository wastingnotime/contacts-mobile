# Android Contacts Local Search Filter Loaded Contacts

- pack: android_compose_client
- runtime_targets:
  - android/app
  - external-api/axiom-exp-contacts
- architecture_mode: layered_android_client

## Discovery Scope

Implement a bounded local search slice for the Android contacts client:

- keep the current list, detail, create, edit, and delete flows intact
- add a search interaction on the contacts list surface
- filter the already-loaded contacts locally by the entered query
- keep the filter deterministic and independent from backend search support
- preserve the currently selected contact and form flows when search is toggled

This slice does not add a new backend endpoint. It makes the loaded list easier to scan and narrows the visible set without changing the read/write API contract.

## Use-Case Contract

The app should expose one local list-filtering interaction:

- `FilterContacts`

It takes the current loaded contacts and a query string, and returns the filtered list the UI should render.

## Main Business Rules

- filtering happens entirely on the client against the currently loaded contacts
- query matching should be case-insensitive and should cover the visible contact text used in the list
- clearing the query restores the full loaded list
- an empty query should not introduce a new backend request or alter detail/create/edit state
- if no contacts match the query, the screen should show an explicit empty-filter state rather than pretending the backend returned no data

## Required Ports

- no new backend port is required
- the existing contacts list state and UI composition are the main integration points

## Initial Test Plan

- verify the filter logic returns only matching contacts for representative queries
- verify the filter is case-insensitive
- verify clearing the query restores the full list
- verify the list screen shows an explicit filtered-empty state when nothing matches
- verify detail, create, edit, and delete flows remain reachable when search is not active

## Scenario Definition

Given a loaded contacts list, when the user enters a search query, the Android app should immediately narrow the visible contacts without contacting the backend.

When the query is cleared, the app should return to the full loaded list. If no contacts match, the UI should show a filtered-empty state that is distinct from the backend-empty state.

## Done Criteria

- the app module compiles in the Android Gradle project shape
- the contacts list supports deterministic local filtering
- the filtered-empty state is visible and distinct from the backend-empty state
- deterministic tests cover query matching, empty results, and query clearing
- the existing read/write flows continue to work without backend changes
