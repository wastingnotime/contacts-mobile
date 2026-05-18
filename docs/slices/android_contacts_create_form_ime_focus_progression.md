# Android Contacts Create Form IME Focus Progression

- pack: android_compose_client
- runtime_targets:
  - android/app
- architecture_mode: layered_android_client

## Discovery Scope

Implement a narrow create-form interaction refinement for the Android contacts client:

- open the create-contact form from the main contacts screen
- move focus from first name to last name when the user advances with the keyboard
- move focus from last name to phone number when the user advances with the keyboard
- submit the form from the phone number field when the user completes entry
- keep the existing create validation, backend submission, and success/failure flow intact

This slice changes input navigation behavior only. It does not change the backend contract, the create success screen, or the post-submit navigation policy.

## Use-Case Contract

The app should expose one local create-form interaction:

- create-form keyboard progression

It moves focus through the required fields in a predictable order and lets the user complete the form without needing to tap between fields.

## Main Business Rules

- first name is the first field in the create form
- last name is the second field in the create form
- phone number is the final field in the create form
- advancing from one field should move to the next field when one exists
- completing the final field should submit the form
- validation rules for required fields remain unchanged
- create submission and create success behavior remain unchanged
- the keyboard progression should not alter field validation semantics

## Required Ports

- no new backend ports
- no new repository ports

## Initial Test Plan

- verify the create form exposes a next-action progression from first name to last name
- verify the create form exposes a next-action progression from last name to phone number
- verify the phone number field completes the create action
- verify validation still blocks incomplete submission
- verify the existing create submission path still reaches the backend use case
- verify no change to create-success navigation

## Scenario Definition

Given a user opens the create-contact form on the contacts screen, the keyboard interaction should advance through first name, last name, and phone number in order, and the final field should complete the form submission without requiring extra taps.

If the user leaves a required field blank, the form should still report validation normally and should not bypass the existing create error handling.

## Done Criteria

- the create form advances focus across fields in a deterministic order
- the final field can submit the form through the existing create action
- deterministic tests cover the focus progression and unchanged validation behavior
- the repository keeps create success navigation as a separate slice
