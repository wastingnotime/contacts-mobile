# Android Contacts App Preview Text Stress Matrix

- pack: android_compose_client
- runtime_targets:
  - android/app
- architecture_mode: layered_android_client

## Discovery Scope

Expand the design-time preview surface for the Android contacts app shell so Android Studio can inspect how the main visible states handle long contact text.

This slice keeps the product behavior unchanged and focuses only on how the app shell is exposed in design time when names, titles, and phone numbers are longer than the average contact record.

The preview coverage should make it easy to inspect:

- a contacts list with long display names
- a contacts list with long phone numbers
- a contact detail with long field values
- the same stress cases in the existing light and dark themes

## Use-Case Contract

No new business use case is introduced.

This slice does not change app behavior. It refines the way existing screen states are exposed in design time using text-heavy preview data.

## Main Business Rules

- the preview should represent existing screen states honestly
- the preview should make text wrapping, truncation, and top-bar pressure visible
- the preview should not rely on repository, network, or view-model side effects
- the runtime app behavior should remain unchanged

## Required Ports

None.

This is a UI exposure slice only.

## Initial Test Plan

- verify the app module still compiles after adding text-stress preview helpers
- verify the preview composables are deterministic and do not require runtime state
- verify the app shell can render representative stress cases for list and detail text

## Scenario Definition

Given the contacts app entrypoint is opened in Android Studio design time, the preview surface should show how the UI behaves when contact names and phone numbers are unusually long.

Given a developer is iterating on the main activity or shell UI, the preview should expose the text-heavy state combinations that matter for list and detail evolution.

## Done Criteria

- the app module still compiles in the Android Gradle project shape
- `ContactsApp` has preview coverage for long-text list and detail cases
- the preview helpers remain deterministic and isolated from runtime data access
- the repository documents the text-stress exposure boundary explicitly
