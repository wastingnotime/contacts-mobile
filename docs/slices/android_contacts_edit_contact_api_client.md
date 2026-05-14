# Android Contacts Edit Contact API Client

- pack: android_compose_client
- runtime_targets:
  - android/app
  - external-api/axiom-exp-contacts
- architecture_mode: layered_android_client

## Discovery Scope

Implement the next product slice for the Android contacts client:

- open an existing contact detail
- navigate to an edit-contact form
- submit the updated contact to `PUT /contacts/{id}`
- render editing, success, and failure states for the edit flow
- keep the existing list, detail, and create flows intact

This slice extends the current read-and-create client into an edit-capable product slice. It does not change the emulator smoke-test guidance or the backend simulation workflow.

## Use-Case Contract

The app should expose one edit use case:

- `UpdateContact`

It submits the edited form data to `PUT /contacts/{id}`, maps the transport response into app models, and returns a state the UI can render.

## Main Business Rules

- the edit form starts from the selected contact's current first name, last name, and phone number
- the backend persists the update and returns the updated contact on success
- a successful update should keep the edited contact visible and update any list representation that reflects the contact
- transport failures should remain explicit and retryable
- the current list, detail, and create behaviors should not regress

## Required Ports

- `ContactsRepository`
- `ContactsApiClient`
- optional configuration port for base URL selection

## Initial Test Plan

- verify transport JSON is mapped from edit form input into the update request and success response
- verify `UpdateContact` returns an updated contact from a fake repository
- verify the edit form is prefilled from the selected contact detail
- verify repository failures become the edit error state
- verify the updated contact is reflected back into the visible app flow

## Scenario Definition

Given a user opens a contact detail and chooses edit, the Android app should show a form prefilled with the current contact data, send `PUT /contacts/{id}` on submit, and show a successful outcome when the backend accepts the update.

If the backend rejects the request or is unavailable, the screen should remain honest about the failure and offer a retry path without disturbing the existing list, detail, or create flows.

## Done Criteria

- the app module compiles in the Android Gradle project shape
- the app can submit an updated contact through the backend API contract
- deterministic tests cover edit input, transport mapping, and success/failure behavior
- the repository documents the Android pack and update boundary explicitly

