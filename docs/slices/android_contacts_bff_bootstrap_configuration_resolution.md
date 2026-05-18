# Android Contacts BFF Bootstrap Configuration Resolution

- pack: android_compose_client
- runtime_targets:
  - android/app
  - go/bff
  - external-api/axiom-exp-contacts
- architecture_mode: layered_android_client

## Discovery Scope

Keep the current repository-owned BFF startup behavior intact while moving `BuildConfig` mapping into one explicit configuration resolver:

- preserve the current contacts flows and bootstrap assembly
- resolve the repository-owned BFF bootstrap configuration from app build values in one place
- keep `MainActivity` thin by handing it an already-resolved bootstrap configuration
- keep invalid repository-owned BFF startup values failing early and clearly

## Use-Case Contract

No new domain use case is introduced.

This slice changes the startup configuration path for the existing contacts use cases:

- `LoadContacts`
- `LoadContactById`
- `CreateContact`
- `UpdateContact`
- `DeleteContact`

The use cases should still run exactly as before, but the bootstrap configuration should come from a dedicated resolver instead of being assembled inline in the activity or bootstrapper.

## Main Business Rules

- configuration mapping should happen once and in one place
- the bootstrapper should receive explicit startup configuration instead of raw build values
- the activity should remain thin and only delegate startup
- invalid configuration should fail early rather than producing a partially wired app

## Required Ports

- `ContactsRepository`
- `ContactsBffClient`
- optional startup configuration resolver or value object for repository-owned BFF bootstrapping

## Initial Test Plan

- verify the startup configuration resolver maps build values into the bootstrap configuration
- verify invalid build values fail early during configuration resolution
- verify the bootstrapper still assembles the client and repository from resolved configuration
- verify `MainActivity` delegates through the resolver and bootstrap seam

## Scenario Definition

Given valid build values, the app should resolve repository-owned BFF startup configuration once, bootstrap the contacts client, and reach the existing UI with no visible behavior change.

If build values are invalid, the resolver or bootstrap should fail clearly before the app tries to construct a partial client.

## Done Criteria

- the app module compiles in the Android Gradle project shape
- BuildConfig mapping lives in one explicit bootstrap-configuration resolver
- deterministic tests cover valid and invalid startup configuration resolution
- the repository documents the Android pack and repository-owned BFF startup boundary explicitly
