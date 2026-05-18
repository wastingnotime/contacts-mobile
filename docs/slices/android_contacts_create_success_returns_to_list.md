# Android Contacts Create Success Returns To List

- pack: android_compose_client
- runtime_targets:
  - android/app
- architecture_mode: layered_android_client

## Discovery Scope

Implement a narrow create-flow navigation refinement for the Android contacts client:

- open the create-contact form from the main contacts screen
- submit a valid new contact through the existing create flow
- show the existing create success state
- resolve the success surface back to the contacts list
- keep the created contact visible in the list so it can be opened from there
- keep the existing create validation, transport, and failure behavior intact

This slice changes post-create navigation policy only. It does not change the backend contract, the create form fields, or the create request/response mapping.

## Use-Case Contract

The app should expose one local create-success navigation rule:

- create success returns to the list

It closes the create success surface after a successful create and relies on the list to expose the newly created contact.

## Main Business Rules

- create still starts from the list surface
- a successful create should preserve the new contact in the visible list
- the user should return to the list after acknowledging create success
- the created contact should remain reachable from the list by normal navigation
- create validation and backend failure handling remain unchanged
- edit success behavior is out of scope for this slice
- the success surface should not become a separate permanent destination

## Required Ports

- no new backend ports
- no new repository ports

## Initial Test Plan

- verify a successful create still inserts the new contact into the visible list
- verify the create success state resolves back to the list when acknowledged
- verify the newly created contact remains reachable from the list
- verify create validation and failure behavior remain unchanged
- verify the create transport path still reaches the backend use case
- verify the success flow does not alter create request mapping

## Scenario Definition

Given a user opens the create-contact form, submits a valid contact, and receives success, the app should return to the contacts list with the new contact visible there.

If the user wants to inspect the newly created contact, they should do so from the list rather than through a separate success-step detour.

## Done Criteria

- create success returns the user to the list surface
- the created contact remains visible and navigable from the list
- deterministic tests cover the success-state resolution and list visibility
- the repository keeps edit-success cleanup as a separate slice
