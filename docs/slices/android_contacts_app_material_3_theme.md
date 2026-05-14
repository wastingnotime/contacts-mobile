# Android Contacts App Material 3 Theme

- pack: android_compose_client
- runtime_targets:
  - android/app
- architecture_mode: layered_android_client

## Discovery Scope

Implement a theme layer for the Android contacts app that aligns runtime UI and previews with Material 3 guidance:

- apply a shared Compose Material 3 app theme
- keep the current light and dark appearance paths
- enable dynamic color on supported Android versions
- expose typography and shape tokens through the app theme
- reuse the same theme wrapper for runtime and previews
- keep existing screen behavior unchanged

This slice changes style plumbing only. It does not change contacts behavior, navigation, or backend contracts.

## Use-Case Contract

The app should expose one theme contract:

- Contacts app theme

It provides a single Material 3 theme entrypoint used by the runtime app and by design-time previews.

## Main Business Rules

- the app should continue to support light and dark modes
- supported Android versions should use dynamic color when possible
- previews should render through the same theme wrapper as runtime content
- the existing screens should keep their current layout and behavior
- the theme should stay within Material 3 rather than mixing design systems

## Required Ports

- no backend ports
- no repository ports

## Initial Test Plan

- verify the app theme wrapper compiles in the Android module
- verify runtime and preview code both use the shared theme wrapper
- verify the theme can resolve light and dark color schemes deterministically
- verify the current screen tests still pass under the shared theme

## Scenario Definition

Given the contacts app is launched on a supported Android device, the UI should render through a shared Material 3 theme that can honor dark mode and dynamic color when available.

Given the app is inspected in Android Studio previews, the preview surface should use the same theme wrapper so the design-time appearance stays consistent with runtime.

## Done Criteria

- the app has a shared Material 3 theme entrypoint
- runtime and previews both use that theme
- dynamic color is enabled where supported
- deterministic tests and existing UI checks remain green
