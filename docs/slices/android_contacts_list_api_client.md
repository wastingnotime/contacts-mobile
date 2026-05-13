# Android Contacts List API Client

- pack: android_compose_client
- runtime_targets:
  - android/app
  - external-api/axiom-exp-contacts
- architecture_mode: layered_android_client

## Discovery Scope

Implement the first native Android slice for the contacts product:

- open the app
- load contacts from the external `axiom-exp-contacts` API
- render loading, empty, error, and list states
- keep the API base URL configurable for emulator and local use

## Use-Case Contract

The app should expose one initial use case:

- `LoadContacts`

It loads `GET /contacts`, maps the transport payload into app models, and returns a state the UI can render.

## Main Business Rules

- contacts are read from the backend, not locally authored in this slice
- transport uses the backend's snake_case fields
- successful loads render the full contact list
- empty responses render an empty state
- failures preserve an error state with a retry path

## Required Ports

- `ContactsRepository`
- `ContactsApiClient`
- optional configuration port for base URL selection

## Initial Test Plan

- verify transport JSON is mapped from snake_case into app models
- verify `LoadContacts` returns contacts from a fake repository
- verify an empty repository result becomes the empty UI state
- verify repository failures become the error UI state

## Scenario Definition

Given the API returns one contact with `first_name`, `last_name`, and `phone_number` fields, the Android app should display that contact in the list view after loading completes.

If the API is unavailable, the screen should remain honest about the failure and offer a retry.

## Done Criteria

- the app module compiles in the Android Gradle project shape
- the list screen can render contacts from the API contract
- deterministic tests cover mapping and load behavior
- the repository documents the Android pack and API boundary explicitly
