# Android Contacts List Viewport Tracks Nearest Surviving Row

- pack: android_compose_client
- runtime_targets:
  - android/app
  - external-api/axiom-exp-contacts
- architecture_mode: layered_android_client

## Discovery Scope

Refine the contacts list viewport behavior when the row currently anchoring the visible position disappears:

- keep the current list, detail, create, edit, delete, sorting, filtering, search-summary, stale-data, and viewport continuity flows intact
- when the anchor contact disappears after create, update, delete, or refresh, keep the viewport aligned to the nearest surviving row instead of snapping back to the top
- keep the viewport behavior local and deterministic
- avoid any backend endpoint or transport change

This slice makes the viewport recovery rule explicit for list mutations that remove or replace the contact currently acting as the visible anchor.

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

The UI should keep the same visible neighborhood when the current anchor contact is no longer present.

## Main Business Rules

- if the anchor contact still exists, restore the viewport to that contact
- if the anchor contact disappeared, restore the viewport to the closest surviving row at the same relative position when possible
- if the list is shorter than the remembered viewport, clamp to the last available row
- search filtering and list sorting should continue to determine which rows are visible
- the relevant exported contract docs under `contracts/` should remain unchanged

## Required Ports

- no new backend port is required
- the existing presentation state and list UI composition are the main integration points

## Initial Test Plan

- verify the viewport follows the nearest surviving row after deleting the anchored contact
- verify the viewport follows the nearest surviving row after refreshing to a list that no longer contains the anchor
- verify the viewport still restores to the anchor contact when it survives
- verify search, sort, filtered-empty, stale-data, CRUD, and navigation flows remain reachable
- verify the relevant exported contract docs under `contracts/` remain unchanged

## Scenario Definition

Given a loaded contacts list that is scrolled away from the top, when the currently visible anchor contact is removed or replaced by create, update, delete, or refresh, the Android app should keep the user near the same visible neighborhood instead of jumping back to the top.

## Done Criteria

- the app module compiles in the Android Gradle project shape
- the viewport recovery rule is explicit for missing anchors
- deterministic tests cover nearest-row fallback and anchor-preserving behavior
- the existing search, sort, stale-data, CRUD, and navigation flows remain intact
- the relevant exported contract docs under `contracts/` remain unchanged
