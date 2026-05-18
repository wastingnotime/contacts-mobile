# Android Contacts Local Search Visible Fields Only

- pack: android_compose_client
- runtime_targets:
  - android/app
  - external-api/axiom-exp-contacts
- architecture_mode: layered_android_client

## Discovery Scope

Refine the local search behavior for the Android contacts client:

- keep the current list, detail, create, edit, delete, sorting, and search-summary flows intact
- keep search local and deterministic
- restrict matching to the visible contact fields a user can reasonably expect to search by
- exclude hidden/internal identifiers from search matching
- preserve the current filtered-empty behavior and clear-search action

This slice narrows the search semantics without adding any backend endpoint or transport change. It makes the search interaction feel more user-facing and less implementation-driven.

## Use-Case Contract

The app should continue to expose the existing local filter use case:

- `FilterContacts`

Its matching rules should only consider visible contact text fields.

## Main Business Rules

- search remains a local interaction over the loaded contacts
- matching should cover the visible name and phone text the user sees in the list
- internal identifiers should not influence query results
- clearing the query still restores the full sorted list
- filtered-empty and backend-empty states remain distinct

## Required Ports

- no new backend port is required
- the existing local filter logic and list presentation are the main integration points

## Initial Test Plan

- verify a query matches contacts by visible name and phone fields
- verify a query does not match by internal id alone
- verify case-insensitive behavior still works
- verify clearing the query restores the full list
- verify the search summary and filtered-empty states still work

## Scenario Definition

Given a loaded contacts list, when the user enters a query, the Android app should only match against visible contact text such as name and phone number.

If the query only matches an internal identifier, the app should not show a result from that field.

## Done Criteria

- the app module compiles in the Android Gradle project shape
- the local search matches only visible contact fields
- deterministic tests cover visible-field matching and hidden-id exclusion
- the existing search summary, filtered-empty, sort, and CRUD flows remain intact
- the relevant exported contract docs under `contracts/` remain unchanged
