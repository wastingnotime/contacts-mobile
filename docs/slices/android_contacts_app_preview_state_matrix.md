# Android Contacts App Preview State Matrix

- pack: android_compose_client
- runtime_targets:
  - android/app
- architecture_mode: layered_android_client

## Discovery Scope

Expand the design-time surface for the Android contacts client so Android Studio can preview the app shell across the main visible states without requiring a live device run.

The slice should keep the existing product behavior unchanged and focus on previewability of the current Compose entrypoint:

- show the app in a loading state
- show the app with an empty contacts state
- show the app with a populated contacts list
- show the app with a loaded contact detail
- show the app with transient refresh failures visible while content stays on screen

## Use-Case Contract

No new business use case is introduced.

This slice does not change app behavior. It refines the way existing screen states are exposed in design time.

## Main Business Rules

- the preview should represent existing screen states honestly
- the preview should not rely on repository, network, or view-model side effects
- the preview should be easy to scan in Android Studio as the main screen evolves
- the runtime app behavior should remain unchanged

## Required Ports

None.

This is a UI exposure slice only.

## Initial Test Plan

- verify the app module still compiles after adding preview helpers
- verify the preview composables are deterministic and do not require runtime state
- verify the current app shell can render representative loading, empty, loaded, detail, and transient-failure states

## Scenario Definition

Given the contacts app entrypoint is opened in Android Studio design time, the preview surface should make the main UI states visible without launching the app on a device.

Given a developer is iterating on the main activity or shell UI, the preview should expose the state combinations that matter for contacts list and detail evolution.

## Done Criteria

- the app module still compiles in the Android Gradle project shape
- `ContactsApp` or the closest app-shell composable has preview coverage for the main visible states
- the preview helpers remain deterministic and isolated from runtime data access
- the repository documents the design-time exposure boundary explicitly
