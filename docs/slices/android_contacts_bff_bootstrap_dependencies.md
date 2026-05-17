# Android Contacts BFF Bootstrap Dependencies

- pack: android_compose_client
- runtime_targets:
  - android/app
  - go/bff
  - external-api/axiom-exp-contacts
- architecture_mode: layered_android_client

## Discovery Scope

Keep the current BFF startup behavior intact while moving the resolved BFF dependency graph into one explicit interface-layer value object:

- preserve the existing contacts flows and startup behavior
- keep raw build-value sourcing and startup configuration resolution unchanged
- assemble the resolved BFF client and repository in one place
- keep `ContactsBffBootstrapper` focused on turning dependencies into the final UI bootstrap

## Use-Case Contract

No new domain use case is introduced.

This slice changes the resolved dependency graph for the existing contacts use cases:

- `LoadContacts`
- `LoadContactById`
- `CreateContact`
- `UpdateContact`
- `DeleteContact`

The use cases should still run exactly as before, but the app should group the resolved BFF client and repository into one explicit dependency object before creating the view-model factory.

## Main Business Rules

- dependency assembly should happen once and in one place
- the bootstrapper should consume a resolved dependency object rather than assembling the graph inline
- the startup flow should remain explicit from build values to final UI bootstrap
- invalid startup configuration should still fail early in the existing resolution path

## Required Ports

- `ContactsBffClient`
- `ContactsRepository`
- `ContactsBffBootstrapConfigurationResolver`
- `ContactsBffBootstrapBuildConfigurationSource`
- optional BFF dependency value object for startup assembly

## Initial Test Plan

- verify the dependency object groups the resolved BFF client and repository
- verify the bootstrapper still assembles the final UI bootstrap from those resolved dependencies
- verify invalid startup configuration still fails in the existing resolution seam
- verify the build-configuration source and configuration resolver tests still pass unchanged

## Scenario Definition

Given valid build values, the app should source raw startup values once, normalize them once, assemble the resolved BFF dependency graph once, and reach the existing UI with no visible behavior change.

If build values are invalid, the startup flow should fail clearly before the app tries to construct a partial client or repository.

## Done Criteria

- the app module compiles in the Android Gradle project shape
- resolved BFF dependencies live in one explicit interface-layer value object
- deterministic tests cover dependency assembly and the existing startup resolution seams
- the repository documents the Android pack and BFF dependency boundary explicitly
