# Android Contacts Request Claims Headers

- pack: android_compose_client
- runtime_targets:
  - android/app
  - external-api/axiom-exp-contacts
- architecture_mode: layered_android_client

## Discovery Scope

Implement the next Android slice for the contacts product:

- keep the current list and detail flows intact
- attach explicit backend request claims to every contacts request through the repository-owned Go BFF
- keep the claims source configurable for emulator, local-device, and production environments
- preserve the existing transport and UI behavior while making the request boundary explicit

## Use-Case Contract

No new domain use case is introduced.

This slice changes the request boundary for the existing contacts use cases:

- `LoadContacts`
- `LoadContactById`

Both use cases should reach the backend through the repository-owned BFF with explicit claims-style headers.

## Main Business Rules

- contacts requests through the repository-owned BFF should include explicit request claims headers
- the default local claims should be admin-shaped so the app can exercise the backend contract without a login flow
- the app should not invent a browser-style login/session layer just to produce request claims
- transport errors should continue to surface through the existing list and detail failure states

## Required Ports

- `ContactsRepository`
- `ContactsBffClient`
- optional configuration port for auth claims selection

## Initial Test Plan

- verify list requests include the configured request claims headers
- verify detail requests include the configured request claims headers
- verify request claims configuration is resolved from build configuration
- verify blank claims configuration fails early

## Scenario Definition

Given the repository-owned BFF expects claims-style request headers, the Android app should load the list and a selected detail record successfully using its configured request claims.

If the configured claims are invalid or missing, the build or runtime should fail clearly rather than silently sending unauthenticated requests.

## Done Criteria

- the app module compiles in the Android Gradle project shape
- repository-owned BFF requests carry explicit request claims headers
- deterministic tests cover header injection and configuration resolution
- the repository documents the Android pack and request boundary explicitly
