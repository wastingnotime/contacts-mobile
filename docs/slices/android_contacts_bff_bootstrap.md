# Android Contacts BFF Bootstrap

- pack: android_compose_client
- runtime_targets:
  - android/app
  - go/bff
  - external-api/axiom-exp-contacts
- architecture_mode: layered_android_client

## Discovery Scope

Keep the current contacts behavior intact while moving the BFF wiring out of `MainActivity` and into one explicit bootstrap object:

- preserve the current list, detail, create, edit, and delete flows
- keep the BFF base URL, API surface, and auth headers configurable for emulator, local-device, and production environments
- assemble the BFF client and repository through one interface-layer bootstrap seam
- keep `MainActivity` thin and focused on starting the UI

## Use-Case Contract

No new domain use case is introduced.

This slice changes the startup assembly for the existing contacts use cases:

- `LoadContacts`
- `LoadContactById`
- `CreateContact`
- `UpdateContact`
- `DeleteContact`

The use cases should still run exactly as before, but their BFF dependencies should come from one bootstrap object instead of manual activity wiring.

## Main Business Rules

- interface-layer startup should stay explicit and easy to read
- the bootstrap should own BFF dependency assembly, not the screen or use-case layer
- the mobile app should continue to fail clearly if BFF configuration is invalid
- transport mapping and request-claim behavior should remain unchanged

## Required Ports

- `ContactsRepository`
- `ContactsBffClient`
- optional bootstrap configuration port or value object for BFF startup assembly

## Initial Test Plan

- verify the bootstrap assembles the BFF client and repository from configuration
- verify invalid BFF startup configuration fails early
- verify `MainActivity` delegates BFF construction to the bootstrap seam
- verify the existing contacts tests still pass unchanged after the wiring move

## Scenario Definition

Given valid BFF configuration, the app should start through the bootstrap seam and reach the existing contacts screen with the same behavior as before.

If BFF startup configuration is invalid, the app should fail clearly rather than silently constructing a partially configured client.

## Done Criteria

- the app module compiles in the Android Gradle project shape
- BFF wiring lives in one bootstrap seam instead of being assembled in `MainActivity`
- deterministic tests cover bootstrap assembly and invalid configuration behavior
- the repository documents the Android pack and BFF bootstrap boundary explicitly
