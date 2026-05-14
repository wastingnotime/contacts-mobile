# Android Contacts Search Summary And Clear Action

- pack: android_compose_client
- runtime_targets:
  - android/app
  - external-api/axiom-exp-contacts
- architecture_mode: layered_android_client

## Discovery Scope

Implement a bounded usability slice for the Android contacts search experience:

- keep the current list, detail, create, edit, delete, sorting, and filtering flows intact
- make the active search state easier to read on the list surface
- show a visible summary of how many loaded contacts match the current query on both loaded and filtered-empty search surfaces
- provide an explicit clear-search action whenever a non-blank query is active
- keep the search interaction deterministic and local
- avoid any backend search endpoint or transport change

This slice does not change the contacts API contract. It makes the existing local search behavior more legible and easier to recover from when the query narrows the list too far.

## Use-Case Contract

The app should continue to expose the existing list-filtering behavior:

- `FilterContacts`

The UI should use the filtered result set to surface the match count and a clear-search action without adding a new backend dependency.

## Main Business Rules

- search remains a local interaction over the loaded contacts
- the summary should reflect the number of visible contacts that match the current query, including zero matches
- the clear action should reset only the query and restore the full sorted list
- the filtered-empty state should remain distinct from the backend-empty state
- the summary and clear action should not interfere with detail, create, edit, or delete flows

## Required Ports

- no new backend port is required
- the existing presentation state for the contacts list is the main integration point

## Initial Test Plan

- verify the search summary reflects the filtered contact count
- verify the clear action resets the query and restores the full list
- verify the summary does not appear when the query is blank
- verify the filtered-empty state still renders distinctly from backend-empty
- verify the existing read/write flows remain reachable

## Scenario Definition

Given a loaded contacts list, when the user enters a query that matches some contacts, the Android app should show how many loaded contacts match and provide a clear-search action.

Given a loaded contacts list, when the query matches no contacts, the app should still show the search summary together with the filtered-empty state and the clear-search action.

When the user clears the query, the app should restore the full sorted list without contacting the backend.

## Done Criteria

- the app module compiles in the Android Gradle project shape
- the search surface displays a useful match summary when filtering is active
- the clear action restores the unfiltered list
- deterministic tests cover summary visibility and query clearing
- the existing backend contract and other contacts flows remain unchanged
