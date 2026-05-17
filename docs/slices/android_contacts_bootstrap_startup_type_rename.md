# Android Contacts Bootstrap Startup Type Rename

- pack: android_compose_client
- runtime_targets:
  - android/app
  - go/bff
  - external-api/axiom-exp-contacts
- architecture_mode: layered_android_client

## Discovery Scope

Keep the current startup behavior intact while renaming the startup-layer types away from BFF-specific naming:

- preserve the existing contacts flows and startup behavior
- keep the transport-layer BFF client, auth headers, API surface, and repository unchanged
- rename the startup configuration, build-configuration source, dependency object, and their resolvers to app-level names
- keep the final bootstrap object and app-start facade unchanged

## Use-Case Contract

No new domain use case is introduced.

This slice changes only the startup-layer naming for the existing contacts use cases:

- `LoadContacts`
- `LoadContactById`
- `CreateContact`
- `UpdateContact`
- `DeleteContact`

The use cases should still run exactly as before, but the startup configuration and dependency seams should read as app-level objects rather than BFF-branded ones.

## Main Business Rules

- startup-layer names should reflect the app boundary
- transport-layer names should remain BFF-specific where they describe the actual HTTP boundary
- the startup flow should remain explicit and behavior-preserving
- invalid startup configuration should still fail early in the existing resolution path

## Required Ports

- `ContactsBootstrapConfigurationResolver`
- `ContactsBootstrapBuildConfigurationSource`
- `ContactsBootstrapDependencies`
- `ContactsBootstrapDependenciesResolver`
- `ContactsBootstrap`

## Initial Test Plan

- verify the renamed startup-layer types still resolve the app bootstrap correctly
- verify the final bootstrap and app-start facade remain unchanged
- verify invalid startup configuration still fails in the existing resolution seam
- verify the transport-layer client and repository tests still pass unchanged

## Scenario Definition

Given valid build values, the app should source raw startup values once, normalize them once, resolve startup dependencies once, assemble application use cases once, assemble the view-model factory once, wrap the final bootstrap once, and reach the existing UI with no visible behavior change.

If build values are invalid, the startup flow should fail clearly before the app tries to construct a partial client, repository, use-case graph, UI factory, or final bootstrap object.

## Done Criteria

- the app module compiles in the Android Gradle project shape
- startup-layer configuration and dependency types use app-level names
- deterministic tests cover the renamed startup-layer seams
- the repository documents the Android pack and app-start boundary explicitly
