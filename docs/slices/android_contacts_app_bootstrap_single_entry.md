# Android Contacts App Bootstrap Single Entry

- pack: android_compose_client
- runtime_targets:
  - android/app
  - go/bff
  - external-api/axiom-exp-contacts
- architecture_mode: layered_android_client

## Discovery Scope

Keep the current startup behavior intact while narrowing the app-start facade to one entry point:

- preserve the existing contacts flows and startup behavior
- keep raw build-value sourcing, startup normalization, dependency resolution, use-case assembly, factory assembly, final bootstrap assembly, app-start facade, and bootstrap verb unchanged
- expose only one `bootstrap()` entry on the app-start facade
- keep `MainActivity` calling the same single app-start path

## Use-Case Contract

No new domain use case is introduced.

This slice changes only the app-start surface for the existing contacts use cases:

- `LoadContacts`
- `LoadContactById`
- `CreateContact`
- `UpdateContact`
- `DeleteContact`

The use cases should still run exactly as before, but the app-start facade should present only one entry point to avoid implying a second startup mode.

## Main Business Rules

- the app-start facade should present one obvious entry point
- the activity should not imply multiple startup modes
- the startup flow should remain explicit from build values to final UI bootstrap
- invalid startup configuration should still fail early in the existing resolution path

## Required Ports

- `ContactsAppStart`
- `ContactsBffBootstrap`
- `ContactsBffBootstrapper`
- optional single-entry startup facade for the Android entry point

## Initial Test Plan

- verify the app-start facade exposes only `bootstrap()`
- verify `MainActivity` continues to use the single app-start entry point
- verify invalid startup configuration still fails in the existing resolution seam
- verify the build-configuration source, configuration resolver, dependency resolver, use-case assembly, factory assembly, final bootstrap, and app-start tests still pass unchanged

## Scenario Definition

Given valid build values, the app should source raw startup values once, normalize them once, resolve BFF dependencies once, assemble application use cases once, assemble the view-model factory once, wrap the final bootstrap once, hand that bootstrap through one startup facade, invoke `bootstrap()`, and reach the existing UI with no visible behavior change.

If build values are invalid, the startup flow should fail clearly before the app tries to construct a partial client, repository, use-case graph, UI factory, or final bootstrap object.

## Done Criteria

- the app module compiles in the Android Gradle project shape
- the app-start facade exposes a single `bootstrap()` entry point
- deterministic tests cover the app-start facade single entry and the existing startup resolution seams
- the repository documents the Android pack and single-entry app-start boundary explicitly
