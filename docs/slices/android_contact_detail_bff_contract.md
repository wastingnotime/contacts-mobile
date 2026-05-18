# Android Contact Detail BFF Contract

- pack: android_compose_client
- runtime_targets:
  - android/app
  - external-api/axiom-exp-contacts
- architecture_mode: layered_android_client

## Discovery Scope

Implement the next Android slice for the contacts product:

- open the app and load the contacts list
- tap a contact to open a detail view
- load the selected contact from `GET /api/contacts/{id}`
- render loading, detail, not-found, and error states for the detail flow
- keep the BFF base URL configurable for emulator, local-device, and production environments

## Use-Case Contract

The app should expose one detail use case:

- `LoadContactById`

It loads `GET /api/contacts/{id}`, maps the transport payload into app models, and returns a state the UI can render.

## Main Business Rules

- contact detail is read from the backend, not reconstructed only from the list item
- transport uses the backend's snake_case fields
- a valid contact detail renders the full contact record for the selected contact
- a missing contact renders an explicit not-found state
- transport failures preserve an error state with a retry path

## Required Ports

- `ContactsRepository`
- `ContactsBffClient`
- optional configuration port for base URL selection

## Initial Test Plan

- verify transport JSON is mapped from snake_case into app models for a single contact response
- verify `LoadContactById` returns a contact from a fake repository
- verify a missing repository result becomes the not-found detail state
- verify repository failures become the error detail state
- verify tapping a contact in the list navigates to the detail loading flow

## Scenario Definition

Given the BFF returns one contact with `id`, `first_name`, `last_name`, and `phone_number` fields, the Android app should display that contact in a detail view after the user taps the contact in the list.

If the API is unavailable or the contact is missing, the screen should remain honest about the failure and offer a retry or back path.

## Done Criteria

- the app module compiles in the Android Gradle project shape
- the detail screen can render a contact from the API contract
- deterministic tests cover mapping, load behavior, and detail navigation
- the repository documents the Android pack and BFF boundary explicitly
