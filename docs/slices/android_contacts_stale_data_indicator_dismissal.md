# Android Contacts Stale Data Indicator Dismissal

- pack: android_compose_client
- runtime_targets:
  - android/app
  - external-api/axiom-exp-contacts
- architecture_mode: layered_android_client

## Discovery Scope

Refine the transient failure experience for the Android contacts client:

- keep the current list, detail, create, edit, delete, sort, search, and stale-data flows intact
- keep preserving the last known contacts or contact detail when a refresh or reload fails
- allow the user to dismiss the visible stale-data indicator after acknowledging it
- keep the preserved content visible after dismissal
- treat list and detail stale indicators as separate surface-local acknowledgements
- make the dismissal contract deterministic for each surface: list dismissal affects only the list, detail dismissal affects only detail
- preserve the current retry affordance and error messaging

This slice does not change backend behavior. It makes the stale indicator less sticky without hiding the preserved data or changing the retry path.

## Use-Case Contract

No new backend use case is required.

The UI should allow the user to acknowledge and dismiss the stale-data indicator on the list and detail surfaces.

## Main Business Rules

- dismissal only affects the stale indicator, not the preserved content
- a dismissed stale indicator should stay dismissed until the next transient failure or successful reload cycle resets it
- list dismissal must not clear detail acknowledgement, and detail dismissal must not clear list acknowledgement
- a successful reload on one surface only resets acknowledgement for that same surface
- the retry action should remain available even after dismissal
- initial load failures should still remain hard errors rather than stale data

## Required Ports

- no new backend port is required
- the existing presentation state for contacts and contact detail is the main integration point

## Initial Test Plan

- verify the stale indicator is visible after a transient refresh or reload failure
- verify the user can dismiss the stale indicator on list and detail surfaces
- verify dismissal does not hide the preserved content
- verify retry remains available after dismissal
- verify the existing search, sort, and CRUD flows remain reachable

## Scenario Definition

Given preserved list or detail content that is marked stale, when the user dismisses the stale indicator, the Android app should keep the content visible and remove only the warning surface.

The warning should reappear on the next transient failure, and a successful reload for that surface should also clear the acknowledgement.

## Done Criteria

- the app module compiles in the Android Gradle project shape
- stale indicators can be dismissed without hiding preserved content
- deterministic tests cover dismissal and reappearance behavior
- the existing retry, search, sort, and CRUD flows remain intact
- the backend contract remains unchanged
