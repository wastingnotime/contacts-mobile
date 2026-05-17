# Android Contacts List BFF Client

- pack: android_compose_client
- runtime_targets:
  - android/app
  - go/bff
  - external-api/axiom-exp-contacts
- architecture_mode: layered_android_client

## Discovery Scope

Implement the first native Android slice for the contacts product:

- open the app
- load contacts through the Go BFF, which forwards to the external `axiom-exp-contacts` API
- render loading, empty, error, and list states
- keep the BFF base URL configurable for emulator and local use

## Use-Case Contract

The app should expose one initial use case:

- `LoadContacts`

It loads `GET /contacts` from the Go BFF, maps the transport payload into app models, and returns a state the UI can render.

## Main Business Rules

- contacts are read through the BFF, not locally authored in this slice
- transport uses the backend's snake_case fields
- successful loads render the full contact list
- empty responses render an empty state
- failures preserve an error state with a retry path
- the mobile app should remain decoupled from direct contacts-api transport changes

## Required Ports

- `ContactsRepository`
- `ContactsBffClient`
- optional configuration port for BFF base URL selection

## Initial Test Plan

- verify transport JSON is mapped from snake_case into app models
- verify `LoadContacts` returns contacts from a fake repository
- verify an empty repository result becomes the empty UI state
- verify repository failures become the error UI state
- verify the BFF client target is configurable without changing the UI layer

## Scenario Definition

Given the Go BFF returns one contact with `first_name`, `last_name`, and `phone_number` fields, the Android app should display that contact in the list view after loading completes.

If the BFF is unavailable, the screen should remain honest about the failure and offer a retry.

## Done Criteria

- the app module compiles in the Android Gradle project shape
- the list screen can render contacts from the BFF contract
- deterministic tests cover mapping and load behavior
- the repository documents the Android pack and BFF boundary explicitly
