# Android Contacts Sort Loaded Contacts Alphabetically

- pack: android_compose_client
- runtime_targets:
  - android/app
  - external-api/axiom-exp-contacts
- architecture_mode: layered_android_client

## Discovery Scope

Implement a bounded list-ordering slice for the Android contacts client:

- keep the current list, detail, create, edit, delete, and local search flows intact
- normalize the loaded contacts list into a predictable alphabetical order on the client
- order contacts locally by their visible display name, not by backend insertion order
- keep the ordering deterministic across refreshes and local mutations
- preserve the current backend contract and avoid introducing a new endpoint

This slice does not add backend sorting support. It makes the loaded list easier to scan by ensuring the visible order is stable and alphabetical.

## Use-Case Contract

The app should expose one list-ordering use case:

- `SortContacts`

It takes the current loaded contacts and returns the deterministically ordered list the UI should render.

## Main Business Rules

- ordering happens entirely on the client against the currently loaded contacts
- contacts should be ordered by their visible display name in ascending alphabetical order
- tie-breaking should remain deterministic when display names are equal or blank
- sorting should not alter detail, create, edit, delete, or search behavior
- refreshes and local mutations should preserve the same ordering rule

## Required Ports

- no new backend port is required
- the existing contacts list state and UI composition are the main integration points

## Initial Test Plan

- verify the ordering logic sorts representative contacts alphabetically
- verify blank or duplicate display names still produce deterministic order
- verify loaded contacts remain sorted after refresh and local list mutations
- verify local search continues to operate over the sorted list
- verify detail, create, edit, and delete flows remain reachable

## Scenario Definition

Given a loaded contacts list in arbitrary backend order, when the app renders the list, the visible contacts should appear in alphabetical order by display name.

When the user refreshes, creates, edits, or deletes contacts, the same local ordering rule should continue to apply without introducing a backend sort request.

## Done Criteria

- the app module compiles in the Android Gradle project shape
- the contacts list renders in a deterministic alphabetical order
- the ordering remains stable across refreshes and local mutations
- deterministic tests cover ordering, tie-breaking, and interactions with the existing list flow
- the relevant exported contract docs under `contracts/` remain unchanged
