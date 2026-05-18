# Android Contacts Delete Confirmation

- pack: android_compose_client
- runtime_targets:
  - android/app
- architecture_mode: layered_android_client

## Discovery Scope

Implement a narrow delete-flow refinement for the Android contacts client:

- open an existing contact detail
- request delete from the detail surface
- show an explicit confirmation step before the destructive action executes
- proceed with the existing delete flow only after confirmation
- allow cancellation without changing the selected contact
- keep the existing delete success and failure behavior intact

This slice changes the decision gate for delete. It does not change the backend contract, the delete transport, or the list/detail state transitions after the delete is confirmed.

## Use-Case Contract

The app should expose one local delete interaction:

- delete confirmation

It guards the existing `DeleteContact` action with an explicit user confirmation before the app issues the backend delete.

## Main Business Rules

- delete remains available only from an opened contact detail
- the user must explicitly confirm before the backend delete executes
- cancelling confirmation must leave the current contact detail visible
- confirming delete should continue to remove the contact from the visible list and exit detail view on success
- a failed delete should remain explicit and retryable while keeping the selected contact visible
- current list, create, and edit behaviors should not regress
- the confirmation step should not alter the delete transport contract

## Required Ports

- no new backend ports
- no new repository ports

## Initial Test Plan

- verify the delete action first presents a confirmation surface instead of immediately executing
- verify cancelling confirmation leaves the current contact detail unchanged
- verify confirming the action still invokes the existing delete flow
- verify successful delete still removes the contact and returns to the list
- verify delete failure behavior remains explicit and retryable after confirmation
- verify confirmation does not change the BFF delete route

## Scenario Definition

Given a user opens a contact detail and chooses delete, the Android app should ask for confirmation before it issues `DELETE /api/contacts/{id}`.

If the user cancels, the detail view should remain visible and no backend delete should occur.

If the user confirms, the app should continue with the existing delete flow and preserve the current success and failure behavior.

## Done Criteria

- delete is gated by an explicit confirmation step
- cancellation preserves the selected contact detail
- deterministic tests cover confirmation, cancellation, and confirmed delete behavior
- the repository keeps create-success navigation and edit-success cleanup as separate slices
