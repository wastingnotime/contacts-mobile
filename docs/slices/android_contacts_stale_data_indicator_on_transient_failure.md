# Android Contacts Stale Data Indicator On Transient Failure

- pack: android_compose_client
- runtime_targets:
  - android/app
  - external-api/axiom-exp-contacts
- architecture_mode: layered_android_client

## Discovery Scope

Refine the transient failure experience for the Android contacts client:

- keep the current list, detail, create, edit, delete, sort, and search flows intact
- keep preserving the last known contacts or contact detail when a refresh or reload fails
- make stale preserved data explicitly visible to the user
- distinguish fresh loaded data from preserved stale data in the UI
- preserve the current retry affordance and error messaging

This slice does not change backend behavior. It makes the existing stale-data preservation rule easier to understand by adding an explicit freshness indicator to the preserved content.

## Use-Case Contract

No new backend use case is required.

The UI should show a freshness indicator when already-loaded contacts or contact detail are being displayed after a transient failure.

## Main Business Rules

- a transient failure should keep already-loaded data visible
- preserved content should be marked as stale, not merely shown with an error banner
- stale indication should be visible on both the list and detail surfaces
- the retry action should remain available
- initial load failures should still remain hard errors rather than stale data

## Required Ports

- no new backend port is required
- the existing presentation state for contacts and contact detail is the main integration point

## Initial Test Plan

- verify the list surface shows a stale indicator after a refresh failure
- verify the detail surface shows a stale indicator after a reload failure
- verify the stale indicator does not appear on fresh initial loads
- verify retry still works from the stale state
- verify the existing search, sort, create, edit, and delete flows remain reachable

## Scenario Definition

Given already-loaded contacts, when a refresh fails, the Android app should keep the content visible and show that the content is stale.

Given already-loaded contact detail, when a reload fails, the app should keep the detail visible and show that the detail is stale.

## Done Criteria

- the app module compiles in the Android Gradle project shape
- stale preserved data is visibly distinct from fresh loaded data
- deterministic tests cover stale indicators on list and detail surfaces
- the existing retry, search, sort, and CRUD flows remain intact
- the backend contract remains unchanged
