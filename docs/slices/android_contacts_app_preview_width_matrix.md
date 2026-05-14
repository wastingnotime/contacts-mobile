# Android Contacts App Preview Width Matrix

- pack: android_compose_client
- runtime_targets:
  - android/app
- architecture_mode: layered_android_client

## Discovery Scope

Expand the design-time preview surface for the Android contacts app shell so Android Studio can inspect the main visible states at representative device widths.

This slice keeps the product behavior unchanged and focuses only on how the app shell is exposed in design time across width variants.

The preview coverage should make it easy to inspect:

- a compact phone width
- an expanded tablet-style width
- the loaded contacts list at those widths
- the loaded contact detail at those widths
- the same states in the existing light and dark themes

## Use-Case Contract

No new business use case is introduced.

This slice does not change app behavior. It refines the way existing screen states are exposed in design time across widths.

## Main Business Rules

- the preview should represent existing screen states honestly
- the preview should show the same shell at compact and expanded widths
- the preview should not rely on repository, network, or view-model side effects
- the runtime app behavior should remain unchanged

## Required Ports

None.

This is a UI exposure slice only.

## Initial Test Plan

- verify the app module still compiles after adding width preview helpers
- verify the preview composables are deterministic and do not require runtime state
- verify the app shell can render representative list and detail states at compact and expanded widths

## Scenario Definition

Given the contacts app entrypoint is opened in Android Studio design time, the preview surface should make the main UI states visible at representative phone and tablet widths without launching the app on a device.

Given a developer is iterating on the main activity or shell UI, the preview should expose the state combinations and width variants that matter for contacts list and detail evolution.

## Done Criteria

- the app module still compiles in the Android Gradle project shape
- `ContactsApp` has preview coverage for the main visible states at compact and expanded widths
- the preview helpers remain deterministic and isolated from runtime data access
- the repository documents the width-exposure boundary explicitly
