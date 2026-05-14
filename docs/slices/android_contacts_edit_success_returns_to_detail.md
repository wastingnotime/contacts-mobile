# Android Contacts Edit Success Returns To Detail

- pack: android_compose_client
- runtime_targets:
  - android/app
- architecture_mode: layered_android_client

## Discovery Scope

Implement a narrow edit-flow navigation refinement for the Android contacts client:

- open an existing contact detail
- open the edit-contact form from detail
- submit a valid contact update through the existing edit flow
- show the existing edit success state
- resolve the success surface back to the contact detail
- keep the edited contact visible in detail after the update
- keep the existing edit validation, transport, and failure behavior intact

This slice changes post-edit navigation policy only. It does not change the backend contract, the edit form fields, or the edit request/response mapping.

## Use-Case Contract

The app should expose one local edit-success navigation rule:

- edit success returns to detail

It closes the edit success surface after a successful update and relies on the detail view to expose the updated contact.

## Main Business Rules

- edit still starts from the selected contact detail
- a successful update should preserve the edited contact in detail
- the user should return to the detail surface after acknowledging edit success
- the updated contact should remain reachable from detail by normal navigation
- edit validation and backend failure handling remain unchanged
- create-success navigation and delete confirmation are out of scope for this slice

## Required Ports

- no new backend ports
- no new repository ports

## Initial Test Plan

- verify a successful edit still updates the visible contact data
- verify the edit success state resolves back to detail when acknowledged
- verify the updated contact remains visible in detail after success
- verify edit validation and failure behavior remain unchanged
- verify the edit transport path still reaches the backend use case

## Scenario Definition

Given a user opens a contact detail, edits the contact, and receives success, the app should return to the contact detail with the updated values visible there.

If the user wants to continue from the updated contact, they should do so from the detail surface rather than through a separate success-step detour.

## Done Criteria

- edit success returns the user to the detail surface
- the edited contact remains visible and navigable from detail
- deterministic tests cover the success-state resolution and detail visibility
- the repository keeps create-success list return as a separate slice
