# Android Contacts App Preview Theme Matrix

- pack: android_compose_client
- runtime_targets:
  - android/app
- architecture_mode: layered_android_client

## Discovery Scope

Expand the design-time preview surface for the Android contacts app shell so Android Studio can inspect the main visible states in both light and dark theme variants.

This slice keeps the product behavior unchanged and focuses only on how the app shell is exposed in design time.

The preview coverage should make it easy to inspect:

- loading
- empty
- loaded contacts
- loaded contacts with a transient refresh warning
- loaded contact detail
- loaded contact detail with a transient refresh warning

## Use-Case Contract

No new business use case is introduced.

This slice does not change app behavior. It refines the way existing screen states are exposed in design time across themes.

## Main Business Rules

- the preview should represent existing screen states honestly
- the preview should show the same shell in light and dark theme variants
- the preview should not rely on repository, network, or view-model side effects
- the runtime app behavior should remain unchanged

## Required Ports

None.

This is a UI exposure slice only.

## Initial Test Plan

- verify the app module still compiles after adding theme preview helpers
- verify the preview composables are deterministic and do not require runtime state
- verify the app shell can render representative states in both light and dark theme variants

## Scenario Definition

Given the contacts app entrypoint is opened in Android Studio design time, the preview surface should make the main UI states visible in both light and dark themes without launching the app on a device.

Given a developer is iterating on the main activity or shell UI, the preview should expose the state combinations and theme variants that matter for contacts list and detail evolution.

## Done Criteria

- the app module still compiles in the Android Gradle project shape
- `ContactsApp` has preview coverage for the main visible states in both light and dark theme variants
- the preview helpers remain deterministic and isolated from runtime data access
- the repository documents the theme-exposure boundary explicitly
