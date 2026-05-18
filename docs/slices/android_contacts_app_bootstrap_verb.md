# Android Contacts App Bootstrap Verb

- pack: android_compose_client
- runtime_targets:
  - android/app
  - go/bff
  - external-api/axiom-exp-contacts
- architecture_mode: layered_android_client

## Discovery Scope

Keep the current startup behavior intact while renaming the app-start method to a more explicit bootstrap verb:

- preserve the existing contacts flows and startup behavior
- keep raw build-value sourcing, startup normalization, dependency resolution, use-case assembly, factory assembly, final bootstrap assembly, and app-start facade unchanged
- rename the app-start entry method from `start()` to `bootstrap()`
- keep `MainActivity` dependent on the app-start facade, but with a more explicit verb

## Use-Case Contract

No new domain use case is introduced.

This slice changes only the startup verb for the existing contacts use cases:

- `LoadContacts`
- `LoadContactById`
- `CreateContact`
- `UpdateContact`
- `DeleteContact`

The use cases should still run exactly as before, but the activity should call `bootstrap()` to make the startup intent explicit.

## Main Business Rules

- startup verbs should describe the action clearly
- the app-start facade should remain the single entry point
- the startup flow should remain explicit from build values to final UI bootstrap
- invalid startup configuration should still fail early in the existing resolution path

## Required Ports

- `ContactsAppStart`
- `ContactsBffBootstrap`
- `ContactsBffBootstrapper`
- optional bootstrap verb rename for the Android entry point

## Initial Test Plan

- verify the app-start facade exposes `bootstrap()` and still returns the final `ContactsBffBootstrap`
- verify `MainActivity` can call the renamed bootstrap verb
- verify invalid startup configuration still fails in the existing resolution seam
- verify the build-configuration source, configuration resolver, dependency resolver, use-case assembly, factory assembly, final bootstrap, and app-start tests still pass unchanged

## Scenario Definition

Given valid build values, the app should source raw startup values once, normalize them once, resolve repository-owned BFF dependencies once, assemble application use cases once, assemble the view-model factory once, wrap the final bootstrap once, hand that bootstrap through one startup facade, invoke `bootstrap()`, and reach the existing UI with no visible behavior change.

If build values are invalid, the startup flow should fail clearly before the app tries to construct a partial client, repository, use-case graph, UI factory, or final bootstrap object.

## Done Criteria

- the app module compiles in the Android Gradle project shape
- the app-start facade exposes an explicit `bootstrap()` verb
- deterministic tests cover the app-start facade verb and the existing startup resolution seams
- the repository documents the Android pack and app-bootstrap boundary explicitly
