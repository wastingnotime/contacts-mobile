# Android Emulator Smoke Test Runbook

- pack: android_compose_client
- runtime_targets:
  - android/app
- architecture_mode: layered_android_client

## Discovery Scope

Document a repeatable emulator smoke-test runbook for the Android contacts client that uses the adjacent runtime sandbox repo for backend simulation.

This slice does not change app behavior. It clarifies the operator steps for validating the released Android client on an emulator.

The runbook should make it explicit that:

- the emulator should run the Android client from this repository
- backend simulation should be prepared in `../runtime-sandbox`
- the runtime sandbox should be used to seed and keep the backend available for the smoke test
- the Android app should be verified against the configured emulator base URL

## Use-Case Contract

No new business use case is introduced.

This slice does not change runtime behavior. It documents the manual validation path.

## Main Business Rules

- backend simulation for emulator smoke tests belongs to `../runtime-sandbox`
- the Android client repo should only document how to connect to that simulation
- the runbook should be concise enough to follow during release validation
- the runbook should not imply that this repo owns the backend simulation

## Required Ports

None.

This is a documentation slice only.

## Initial Test Plan

- verify the runbook names the runtime sandbox repo explicitly
- verify the runbook includes a concrete emulator smoke-test sequence
- verify the runbook stays separate from application runtime code

## Scenario Definition

Given a human is preparing to smoke test the Android client on an emulator, the runbook should point them to the runtime sandbox for backend simulation and then to the Android app for validation.

Given the released contacts client is being checked manually, the runbook should describe a repeatable sequence that keeps the backend simulation and emulator steps clear and separate.

## Done Criteria

- the emulator smoke-test runbook is documented in the repository
- the runbook explicitly references `../runtime-sandbox`
- the documentation makes the validation sequence repeatable
- no runtime app behavior changes are introduced
