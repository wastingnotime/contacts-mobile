# Android Contacts BFF View-Model Factory Assembly

- pack: android_compose_client
- runtime_targets:
  - android/app
  - go/bff
  - external-api/axiom-exp-contacts
- architecture_mode: layered_android_client

## Discovery Scope

Keep the current BFF startup behavior intact while moving the `ContactsViewModelFactory` construction into one explicit interface-layer assembly:

- preserve the existing contacts flows and startup behavior
- keep raw build-value sourcing, startup normalization, dependency resolution, and use-case assembly unchanged
- assemble the `ContactsViewModelFactory` in one place
- keep `ContactsBffBootstrapper` focused on producing the final UI bootstrap object

## Use-Case Contract

No new domain use case is introduced.

This slice changes the startup assembly for the existing contacts use cases:

- `LoadContacts`
- `LoadContactById`
- `CreateContact`
- `UpdateContact`
- `DeleteContact`

The use cases should still run exactly as before, but the app should construct the `ContactsViewModelFactory` through one dedicated assembly object before exposing it to the activity.

## Main Business Rules

- UI factory construction should happen once and in one place
- the bootstrapper should consume a resolved factory rather than constructing it inline
- the startup flow should remain explicit from build values to final UI bootstrap
- invalid startup configuration should still fail early in the existing resolution path

## Required Ports

- `ContactsBffUseCases`
- `ContactsBffBootstrapDependencies`
- `ContactsBffBootstrapConfigurationResolver`
- `ContactsBffBootstrapBuildConfigurationSource`
- optional view-model factory assembly object for BFF startup

## Initial Test Plan

- verify the factory assembly produces a `ContactsViewModelFactory` from assembled use cases
- verify the bootstrapper still produces the final UI bootstrap from the assembled factory
- verify invalid startup configuration still fails in the existing resolution seam
- verify the build-configuration source, configuration resolver, dependency resolver, and use-case assembly tests still pass unchanged

## Scenario Definition

Given valid build values, the app should source raw startup values once, normalize them once, resolve BFF dependencies once, assemble application use cases once, assemble the view-model factory once, and reach the existing UI with no visible behavior change.

If build values are invalid, the startup flow should fail clearly before the app tries to construct a partial client, repository, use-case graph, or UI factory.

## Done Criteria

- the app module compiles in the Android Gradle project shape
- the view-model factory lives in one explicit interface-layer assembly object
- deterministic tests cover factory assembly and the existing startup resolution seams
- the repository documents the Android pack and BFF view-model factory boundary explicitly
