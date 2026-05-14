# Android Contacts App Preview Font Scale Matrix

- pack: android_compose_client
- runtime_targets:
  - android/app
- architecture_mode: layered_android_client

## Discovery Scope

Expand the design-time preview surface for the Android contacts app shell so Android Studio can inspect the main visible states at representative font scales.

This slice keeps the product behavior unchanged and focuses only on how the app shell is exposed in design time across typography scaling variants.

The preview coverage should make it easy to inspect:

- a default font scale
- an enlarged accessibility-style font scale
- the loaded contacts list under those font scales
- the loaded contact detail under those font scales
- the existing light and dark themes combined with the font scale variants

## Use-Case Contract

No new business use case is introduced.

This slice does not change app behavior. It refines the way existing screen states are exposed in design time across font scales.

## Main Business Rules

- the preview should represent existing screen states honestly
- the preview should show the same shell at default and enlarged font scales
- the preview should not rely on repository, network, or view-model side effects
- the runtime app behavior should remain unchanged
- the preview should make text overflow and density pressure visible where it matters

## Required Ports

None.

This is a UI exposure slice only.

## Initial Test Plan

- verify the app module still compiles after adding font scale preview helpers
- verify the preview composables are deterministic and do not require runtime state
- verify the app shell can render representative list and detail states at default and enlarged font scales

## Scenario Definition

Given the contacts app entrypoint is opened in Android Studio design time, the preview surface should make the main UI states visible at representative font scales without launching the app on a device.

Given a developer is iterating on the main activity or shell UI, the preview should expose the state combinations and font scaling variants that matter for contacts list and detail evolution.

## Done Criteria

- the app module still compiles in the Android Gradle project shape
- `ContactsApp` has preview coverage for the main visible states at default and enlarged font scales
- the preview helpers remain deterministic and isolated from runtime data access
- the repository documents the font-scale exposure boundary explicitly
