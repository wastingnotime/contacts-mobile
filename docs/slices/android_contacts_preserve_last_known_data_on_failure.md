# Android Contacts Preserve Last Known Data On Failure

- pack: android_compose_client
- runtime_targets:
  - android/app
  - external-api/axiom-exp-contacts
- architecture_mode: layered_android_client

## Discovery Scope

Implement the next Android slice for the contacts product:

- keep the existing contacts list and contact detail flows intact
- preserve the last successfully loaded contacts or contact detail when a transient reload fails
- surface the failure separately instead of replacing the already-visible content with a blank error screen
- keep the repository-owned BFF base URL and request-claims configuration unchanged

## Use-Case Contract

No new business use case is introduced.

This slice refines the behavior of the existing read use cases:

- `LoadContacts`
- `LoadContactById`
- `RefreshContacts`
- `RetryLoadContacts`

The UI should keep the last known successful data visible while a reload is failing.

## Main Business Rules

- already-loaded contacts should remain visible while a transient reload fails
- already-loaded contact detail should remain visible while a transient detail reload fails
- the failure should still be explicit and retryable
- missing-detail behavior should remain distinct from transient failure behavior

## Required Ports

- `ContactsRepository`
- `ContactsBffClient`

## Initial Test Plan

- verify a refresh failure preserves the last successful contact list
- verify a detail reload failure preserves the last successful contact detail
- verify retry still re-enters loading from the failure state
- verify not-found detail remains distinct from transient detail failure

## Scenario Definition

Given the app has already loaded contacts successfully, a later transient network failure during refresh should not erase the existing list. The user should still see the last known contacts and a visible error path.

Given the app has already opened a contact detail successfully, a later transient failure while refreshing that detail should not erase the existing detail view.

## Done Criteria

- the app module compiles in the Android Gradle project shape
- the UI preserves last known successful data during transient reload failures
- deterministic tests cover list and detail failure preservation behavior
- the repository documents the Android pack and failure-handling boundary explicitly
