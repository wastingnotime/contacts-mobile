# Android Contacts BFF Bootstrap Build Configuration Source

- pack: android_compose_client
- runtime_targets:
  - android/app
  - go/bff
  - external-api/axiom-exp-contacts
- architecture_mode: layered_android_client

## Discovery Scope

Keep the current repository-owned BFF startup behavior intact while moving raw `BuildConfig` reading into one explicit source object:

- preserve the existing contacts flows and bootstrap assembly
- read repository-owned BFF startup values from `BuildConfig` in one place only
- hand the bootstrapper a dedicated raw startup configuration instead of embedding `BuildConfig` access in the bootstrapper
- keep `MainActivity` thin and unchanged as a UI entry point

## Use-Case Contract

No new domain use case is introduced.

This slice changes the startup source for the existing contacts use cases:

- `LoadContacts`
- `LoadContactById`
- `CreateContact`
- `UpdateContact`
- `DeleteContact`

The use cases should still run exactly as before, but the app should obtain raw repository-owned BFF startup values through one dedicated source before those values are normalized by the configuration resolver.

## Main Business Rules

- raw `BuildConfig` access should happen once and in one place
- startup configuration should remain a two-step flow: raw build values, then normalized bootstrap configuration
- the bootstrapper should consume explicit startup inputs rather than directly naming individual `BuildConfig` fields
- invalid startup values should still fail early and clearly

## Required Ports

- `ContactsBffBootstrapConfigurationResolver`
- `ContactsBffBootstrapBuildConfiguration`
- optional build-configuration source or startup value object for repository-owned BFF bootstrapping

## Initial Test Plan

- verify the build-configuration source maps `BuildConfig` values into the raw startup configuration
- verify the bootstrapper still assembles the repository-owned BFF client and repository from the normalized configuration
- verify invalid startup values still fail early in the existing configuration-resolution seam
- verify `MainActivity` continues to delegate through the bootstrap seam without knowing `BuildConfig`

## Scenario Definition

Given valid build values, the app should read them once through a single source object, normalize them through the configuration resolver, and reach the existing UI with no visible behavior change.

If build values are invalid, the startup flow should fail clearly before the app tries to construct a partial client.

## Done Criteria

- the app module compiles in the Android Gradle project shape
- raw `BuildConfig` reading lives in one explicit bootstrap-source seam instead of in the bootstrapper
- deterministic tests cover build-configuration sourcing and the existing normalization seam
- the repository documents the exported mobile contract boundary in `contracts/` explicitly
