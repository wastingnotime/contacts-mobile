# Android Contacts Delete Contact BFF Contract

- pack: android_compose_client
- runtime_targets:
  - android/app
  - external-api/axiom-exp-contacts
- architecture_mode: layered_android_client

## Discovery Scope

Implement the next product slice for the Android contacts client:

- open an existing contact detail
- delete the selected contact through `DELETE /api/contacts/{id}`
- render deleting, success, and failure states for the delete flow
- keep the existing list, detail, create, and edit flows intact

This slice extends the current CRUD-capable client into a delete-capable product slice. It does not change the emulator smoke-test guidance or the backend simulation workflow.

## Use-Case Contract

The app should expose one delete use case:

- `DeleteContact`

It deletes the selected contact from `DELETE /api/contacts/{id}` and returns a state the UI can render.

## Main Business Rules

- delete is only available from an opened contact detail
- a successful delete removes the contact from the visible list and returns the app to the list view
- a failed delete should remain explicit and retryable while keeping the selected contact visible
- the current list, detail, create, and edit behaviors should not regress

## Required Ports

- `ContactsRepository`
- `ContactsBffClient`
- optional configuration port for base URL selection

## Initial Test Plan

- verify the delete request is sent to `DELETE /api/contacts/{id}`
- verify `DeleteContact` completes through a fake repository
- verify the delete action is only available from the detail view
- verify repository failures keep the contact detail visible with a failure state
- verify successful delete removes the contact from the visible list and exits detail view

## Scenario Definition

Given a user opens a contact detail and chooses delete, the Android app should send `DELETE /api/contacts/{id}` and remove the contact from the visible list when the BFF accepts the deletion.

If the backend rejects the request or is unavailable, the screen should remain honest about the failure and keep the selected contact visible so the user can retry or go back.

## Done Criteria

- the app module compiles in the Android Gradle project shape
- the app can delete a contact through the BFF contract
- deterministic tests cover delete request mapping, success, and failure behavior
- the repository documents the Android pack and delete boundary explicitly
