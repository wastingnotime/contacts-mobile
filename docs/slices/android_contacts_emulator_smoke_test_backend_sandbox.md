# Android Contacts Emulator Smoke Test Backend Sandbox

- pack: android_compose_client
- runtime_targets:
  - android/app
- architecture_mode: layered_android_client

## Discovery Scope

Clarify the exposure workflow for Android emulator smoke tests so backend simulation comes from the adjacent runtime sandbox repo instead of being improvised inside this repository.

This slice does not change app behavior. It refines the exposure boundary and the operator guidance for running emulator smoke tests against the contacts API.

The slice should make it explicit that:

- emulator smoke tests need a backend simulation source
- that backend simulation source lives in `../runtime-sandbox`
- the Android client should still be exercised against the configured API base URL
- the app behavior under smoke test remains the same as the released slice

## Use-Case Contract

No new business use case is introduced.

This slice does not change runtime behavior. It clarifies the exposure workflow for manual emulator validation.

## Main Business Rules

- backend simulation for emulator smoke tests should come from `../runtime-sandbox`
- the client repository should not own the backend simulation implementation
- the emulator should still observe the contacts client through its configured base URL
- the exposure guidance should stay separate from product logic

## Required Ports

None.

This is an exposure-guidance slice only.

## Initial Test Plan

- verify the repository documents the sandbox-backed emulator smoke-test path
- verify the exposure notes and living evidence point to `../runtime-sandbox`
- verify no production code changes are needed

## Scenario Definition

Given an Android emulator smoke test is being prepared, the guidance should point the operator at `../runtime-sandbox` for backend simulation rather than asking the client repo to simulate the backend itself.

Given the released contacts slice is being manually validated, the emulator should still drive the Android client against the configured API base URL while the backend simulation comes from the sandbox repo.

## Done Criteria

- the backend simulation source for emulator smoke tests is documented explicitly
- the exposure and living artifacts point to `../runtime-sandbox`
- the repository documents the exposure boundary clearly
- no runtime app behavior changes are introduced
