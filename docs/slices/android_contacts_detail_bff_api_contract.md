# Android Contacts Detail BFF API Contract

- pack: android_compose_client
- runtime_targets:
  - android/app
  - go/bff
  - external-api/axiom-exp-contacts
- architecture_mode: layered_android_client

## Discovery Scope

Keep the selected-contact detail experience intact while making the detail fetch contract explicit at the BFF boundary:

- open the app and tap a contact in the list
- load the selected contact from `GET /api/contacts/{id}`
- render loading, detail, not-found, and error states for the detail flow
- keep the existing list, create, edit, and delete flows intact
- keep the BFF API surface explicit instead of re-deriving backend paths at the detail call site

## Use-Case Contract

The app should expose one detail use case:

- `LoadContactById`

It loads the selected contact through the BFF-backed repository and returns a state the UI can render.

## Main Business Rules

- contact detail is read through the BFF, not reconstructed only from the list item
- transport still uses the backend's snake_case fields at the mapping boundary
- a valid contact detail renders the full contact record for the selected contact
- a missing contact renders an explicit not-found state
- transport failures preserve an error state with a retry path
- the selected detail route should stay aligned with the BFF `/api/contacts/{id}` contract

## Required Ports

- `ContactsRepository`
- `ContactsBffClient`
- optional BFF API surface configuration

## Initial Test Plan

- verify transport JSON is mapped from snake_case into app models for a single contact response
- verify `LoadContactById` returns a contact from a fake repository
- verify a missing repository result becomes the not-found detail state
- verify repository failures become the error detail state
- verify tapping a contact in the list opens the BFF-backed detail loading flow
- verify detail requests resolve against `/api/contacts/{id}` through the BFF surface helper

## Scenario Definition

Given the BFF returns one contact with `id`, `first_name`, `last_name`, and `phone_number` fields, the Android app should display that contact in a detail view after the user taps the contact in the list.

If the BFF is unavailable or the contact is missing, the screen should remain honest about the failure and offer a retry or back path.

## Done Criteria

- the app module compiles in the Android Gradle project shape
- the detail screen remains explicitly BFF-backed
- deterministic tests cover mapping, load behavior, route resolution, and detail navigation
- the repository documents the BFF detail boundary explicitly
