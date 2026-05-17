# Android Contacts BFF API Contract

- pack: android_compose_client
- runtime_targets:
  - android/app
  - go/bff
  - external-api/axiom-exp-contacts
- architecture_mode: layered_android_client

## Discovery Scope

Normalize the mobile client onto the fixed Go BFF `/api` surface without changing the user-facing contacts behavior:

- keep the current contacts list, detail, create, edit, and delete flows intact
- route every contacts request through the BFF `/api` prefix instead of assembling backend paths directly in each call site
- keep the Go BFF base URL configurable for emulator, local-device, and production environments
- preserve the current request claims and transport mapping behavior while making the BFF path contract explicit

## Use-Case Contract

No new domain use case is introduced.

This slice changes the transport contract for the existing contacts use cases:

- `LoadContacts`
- `LoadContactById`
- `CreateContact`
- `UpdateContact`
- `DeleteContact`

Each use case should reach the BFF under the same `/api/contacts` contract.

## Main Business Rules

- the mobile app should treat `/api` as the client-facing BFF surface
- contacts behavior should stay unchanged while the transport path contract becomes explicit
- transport still uses the backend's snake_case fields at the mapping boundary
- request claims continue to apply to the same contacts requests
- the client should not construct backend-specific paths outside the BFF gateway seam

## Required Ports

- `ContactsRepository`
- `ContactsBffClient`
- optional configuration port for BFF API surface selection

## Initial Test Plan

- verify list requests resolve against `/api/contacts`
- verify detail requests resolve against `/api/contacts/{id}`
- verify create, update, and delete requests still use the same `/api` prefix
- verify the BFF API surface is resolved from configuration rather than being duplicated at call sites
- verify transport mapping and claims behavior stay unchanged

## Scenario Definition

Given the Go BFF exposes contacts routes beneath `/api`, the Android app should continue to load, inspect, create, edit, and delete contacts without any visible behavior change.

If the BFF path contract is misconfigured or missing, the client should fail clearly rather than silently constructing backend routes itself.

## Done Criteria

- the app module compiles in the Android Gradle project shape
- contacts requests consistently use the fixed BFF `/api` surface
- deterministic tests cover the explicit path contract and preserve the existing transport behavior
- the repository documents the Android pack and BFF contract boundary explicitly
