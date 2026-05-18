# Android Contacts Create Contact BFF Contract

- pack: android_compose_client
- runtime_targets:
  - android/app
  - external-api/axiom-exp-contacts
- architecture_mode: layered_android_client

## Discovery Scope

Implement the next product slice for the Android contacts client:

- open the app
- navigate to a create-contact form
- submit a new contact to `POST /api/contacts`
- render submitting, success, and failure states for the create flow
- keep the existing list and detail flows intact

This slice extends the current read-first client into a write-capable product slice. It does not change the emulator smoke-test guidance or the backend simulation workflow.

## Use-Case Contract

The app should expose one create use case:

- `CreateContact`

It submits the form data to `POST /api/contacts`, maps the transport response into app models, and returns a state the UI can render.

## Main Business Rules

- first name, last name, and phone number are the required contact fields for creation
- the backend generates the new contact identity on success
- a successful create should lead to a visible confirmation state and make the new contact reachable in the app flow
- transport failures should remain explicit and retryable
- the current list and detail read behaviors should not regress

## Required Ports

- `ContactsRepository`
- `ContactsBffClient`
- optional configuration port for base URL selection

## Initial Test Plan

- verify transport JSON is mapped from form input into the create request and success response
- verify `CreateContact` returns a created contact from a fake repository
- verify missing required fields become an explicit validation state before submission
- verify repository failures become the create error state
- verify the created contact can be reached from the success path

## Scenario Definition

Given a user opens the create-contact form, enters a first name, last name, and phone number, and submits the form, the Android app should send `POST /api/contacts` and show a successful outcome when the BFF accepts the new contact.

If the backend rejects the request or is unavailable, the screen should remain honest about the failure and offer a retry path without disturbing the existing list and detail flows.

## Done Criteria

- the app module compiles in the Android Gradle project shape
- the app can submit a new contact through the BFF contract
- deterministic tests cover create input, transport mapping, and success/failure behavior
- the repository documents the Android pack and write boundary explicitly
