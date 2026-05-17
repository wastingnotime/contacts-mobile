# Android Contacts App Start Facade

- pack: android_compose_client
- runtime_targets:
  - android/app
  - go/bff
  - external-api/axiom-exp-contacts
- architecture_mode: layered_android_client

## Discovery Scope

Keep the current startup behavior intact while renaming the startup facade to an app-level object:

- preserve the existing contacts flows and startup behavior
- keep raw build-value sourcing, startup normalization, dependency resolution, use-case assembly, factory assembly, and final bootstrap assembly unchanged
- expose one app-start facade that returns the final `ContactsBffBootstrap`
- keep `MainActivity` dependent on a generic app-start object instead of BFF-specific naming

## Use-Case Contract

No new domain use case is introduced.

This slice changes only the startup vocabulary for the existing contacts use cases:

- `LoadContacts`
- `LoadContactById`
- `CreateContact`
- `UpdateContact`
- `DeleteContact`

The use cases should still run exactly as before, but the activity should call an app-level startup facade instead of a BFF-specific startup name.

## Main Business Rules

- startup naming should reflect the app boundary, not the transport boundary
- the activity should not know BFF-specific startup types
- the startup flow should remain explicit from build values to final UI bootstrap
- invalid startup configuration should still fail early in the existing resolution path

## Required Ports

- `ContactsBffBootstrap`
- `ContactsBffBootstrapper`
- `ContactsBffBootstrapConfigurationResolver`
- `ContactsBffBootstrapBuildConfigurationSource`
- optional app-start facade for the Android entry point

## Initial Test Plan

- verify the app-start facade returns the final `ContactsBffBootstrap`
- verify `MainActivity` can depend on the app-start facade instead of BFF-specific naming
- verify invalid startup configuration still fails in the existing resolution seam
- verify the build-configuration source, configuration resolver, dependency resolver, use-case assembly, factory assembly, and final bootstrap tests still pass unchanged

## Scenario Definition

Given valid build values, the app should source raw startup values once, normalize them once, resolve BFF dependencies once, assemble application use cases once, assemble the view-model factory once, wrap the final bootstrap once, hand that bootstrap through one startup facade, and reach the existing UI with no visible behavior change.

If build values are invalid, the startup flow should fail clearly before the app tries to construct a partial client, repository, use-case graph, UI factory, or final bootstrap object.

## Done Criteria

- the app module compiles in the Android Gradle project shape
- the Android entry point uses one app-level startup facade
- deterministic tests cover the app-start facade and the existing startup resolution seams
- the repository documents the Android pack and app-start boundary explicitly
