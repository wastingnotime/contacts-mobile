# Android Contacts BFF View-Model Factory Bootstrap

- pack: android_compose_client
- runtime_targets:
  - android/app
  - go/bff
  - external-api/axiom-exp-contacts
- architecture_mode: layered_android_client

## Discovery Scope

Keep the current repository-owned BFF startup behavior intact while moving the final `ContactsBffBootstrap` wrapping step into one explicit interface-layer assembly:

- preserve the existing contacts flows and startup behavior
- keep raw build-value sourcing, startup normalization, dependency resolution, use-case assembly, and factory assembly unchanged
- wrap the final `ContactsViewModelFactory` in one place
- keep `ContactsBffBootstrapper` focused on returning the final bootstrap object only

## Use-Case Contract

No new domain use case is introduced.

This slice changes the startup assembly for the existing contacts use cases:

- `LoadContacts`
- `LoadContactById`
- `CreateContact`
- `UpdateContact`
- `DeleteContact`

The use cases should still run exactly as before, but the app should produce the final `ContactsBffBootstrap` through one dedicated assembly object before handing it to the activity.

## Main Business Rules

- the final bootstrap object should be created once and in one place
- the bootstrapper should consume a resolved bootstrap object rather than wrapping it inline
- the startup flow should remain explicit from build values to final UI bootstrap
- invalid startup configuration should still fail early in the existing resolution path

## Required Ports

- `ContactsBffUseCases`
- `ContactsBffBootstrapDependencies`
- `ContactsBffBootstrapConfigurationResolver`
- `ContactsBffBootstrapBuildConfigurationSource`
- optional final bootstrap assembly object for repository-owned BFF startup

## Initial Test Plan

- verify the final bootstrap assembly produces `ContactsBffBootstrap`
- verify the bootstrapper still returns the final bootstrap object from the assembled wrapper
- verify invalid startup configuration still fails in the existing resolution seam
- verify the build-configuration source, configuration resolver, dependency resolver, use-case assembly, and factory assembly tests still pass unchanged

## Scenario Definition

Given valid build values, the app should source raw startup values once, normalize them once, resolve repository-owned BFF dependencies once, assemble application use cases once, assemble the view-model factory once, wrap the final bootstrap once, and reach the existing UI with no visible behavior change.

If build values are invalid, the startup flow should fail clearly before the app tries to construct a partial client, repository, use-case graph, UI factory, or final bootstrap object.

## Done Criteria

- the app module compiles in the Android Gradle project shape
- the final bootstrap object lives in one explicit interface-layer assembly object
- deterministic tests cover final bootstrap assembly and the existing startup resolution seams
- the repository documents the exported mobile contract boundary in `contracts/` explicitly
