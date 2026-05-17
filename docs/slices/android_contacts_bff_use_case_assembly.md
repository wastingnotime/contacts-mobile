# Android Contacts BFF Use-Case Assembly

- pack: android_compose_client
- runtime_targets:
  - android/app
  - go/bff
  - external-api/axiom-exp-contacts
- architecture_mode: layered_android_client

## Discovery Scope

Keep the current BFF startup behavior intact while moving the application use-case construction into one explicit interface-layer assembly:

- preserve the existing contacts flows and startup behavior
- keep raw build-value sourcing, startup normalization, and dependency resolution unchanged
- assemble `LoadContacts`, `LoadContactById`, `CreateContact`, `UpdateContact`, and `DeleteContact` in one place
- keep `ContactsBffBootstrapper` focused on producing the final UI bootstrap object

## Use-Case Contract

No new domain use case is introduced.

This slice changes the startup assembly for the existing contacts use cases:

- `LoadContacts`
- `LoadContactById`
- `CreateContact`
- `UpdateContact`
- `DeleteContact`

The use cases should still run exactly as before, but the app should construct them through one dedicated assembly object before creating the view-model factory.

## Main Business Rules

- use-case construction should happen once and in one place
- the bootstrapper should consume resolved use cases rather than constructing them inline
- the startup flow should remain explicit from build values to final UI bootstrap
- invalid startup configuration should still fail early in the existing resolution path

## Required Ports

- `ContactsRepository`
- `ContactsBffBootstrapDependencies`
- `ContactsBffBootstrapConfigurationResolver`
- `ContactsBffBootstrapBuildConfigurationSource`
- optional use-case assembly object for BFF startup

## Initial Test Plan

- verify the use-case assembly produces the five contacts use cases from a resolved repository
- verify the bootstrapper still produces the final UI bootstrap from assembled use cases
- verify invalid startup configuration still fails in the existing resolution seam
- verify the bootstrap source, configuration resolver, and dependency resolver tests still pass unchanged

## Scenario Definition

Given valid build values, the app should source raw startup values once, normalize them once, resolve BFF dependencies once, assemble application use cases once, and reach the existing UI with no visible behavior change.

If build values are invalid, the startup flow should fail clearly before the app tries to construct a partial client, repository, or use-case graph.

## Done Criteria

- the app module compiles in the Android Gradle project shape
- application use cases live in one explicit interface-layer assembly object
- deterministic tests cover use-case assembly and the existing startup resolution seams
- the repository documents the Android pack and BFF use-case boundary explicitly
