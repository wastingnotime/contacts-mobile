# Android Contacts BFF Bootstrap

- pack: android_compose_client
- runtime_targets:
  - android/app
  - go/bff
  - external-api/axiom-exp-contacts
- architecture_mode: layered_android_client

## Discovery Scope

Keep the current contacts behavior intact while moving the repository-owned BFF wiring out of `MainActivity` and into one explicit bootstrap object:

- preserve the current list, detail, create, edit, and delete flows
- keep the repository-owned BFF base URL, API surface, and auth headers configurable for emulator, local-device, and production environments
- assemble the repository-owned BFF client and repository through one interface-layer bootstrap seam
- keep `MainActivity` thin and focused on starting the UI

## Use-Case Contract

No new domain use case is introduced.

This slice changes the startup assembly for the existing contacts use cases:

- `LoadContacts`
- `LoadContactById`
- `CreateContact`
- `UpdateContact`
- `DeleteContact`

The use cases should still run exactly as before, but their repository-owned BFF dependencies should come from one bootstrap object instead of manual activity wiring.

## Main Business Rules

- interface-layer startup should stay explicit and easy to read
- the bootstrap should own repository-owned BFF dependency assembly, not the screen or use-case layer
- the mobile app should continue to fail clearly if repository-owned BFF configuration is invalid
- transport mapping and request-claim behavior should remain unchanged

## Required Ports

- `ContactsRepository`
- `ContactsBffClient`
- optional bootstrap configuration port or value object for repository-owned BFF startup assembly

## Initial Test Plan

- verify the bootstrap assembles the repository-owned BFF client and repository from configuration
- verify invalid repository-owned BFF startup configuration fails early
- verify `MainActivity` delegates repository-owned BFF construction to the bootstrap seam
- verify the existing contacts tests still pass unchanged after the wiring move

## Scenario Definition

Given valid repository-owned BFF configuration, the app should start through the bootstrap seam and reach the existing contacts screen with the same behavior as before.

If repository-owned BFF startup configuration is invalid, the app should fail clearly rather than silently constructing a partially configured client.

## Done Criteria

- the app module compiles in the Android Gradle project shape
- repository-owned BFF wiring lives in one bootstrap seam instead of being assembled in `MainActivity`
- deterministic tests cover bootstrap assembly and invalid configuration behavior
- the repository documents the Android pack and repository-owned BFF bootstrap boundary explicitly
