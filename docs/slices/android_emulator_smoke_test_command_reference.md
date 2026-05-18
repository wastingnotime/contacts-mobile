# Android Emulator Smoke Test Command Reference

- pack: android_compose_client
- runtime_targets:
  - android/app
- architecture_mode: layered_android_client

## Discovery Scope

Turn the emulator smoke-test guidance into a concrete command reference that points at the adjacent runtime sandbox repo for backend simulation and uses explicit command names rather than generic guidance.

This slice does not change app behavior. It makes the manual validation path easier to execute and repeat.

The command reference should make it explicit that:

- backend simulation lives in `../runtime-sandbox`
- backend seeding and smoke startup should use the sandbox's documented commands
- the Android client validation commands should come after the backend smoke path is live
- the emulator base URL remains the Android client's configured contact API endpoint

## Use-Case Contract

No new business use case is introduced.

This slice documents operator commands for manual validation. It does not change runtime behavior.

## Main Business Rules

- backend simulation for emulator smoke tests should come from `../runtime-sandbox`
- the Android client repository should reference the sandbox commands explicitly
- the command reference should stay short, repeatable, and subordinate to the runbook
- the client repo should not own the backend simulation implementation

## Required Ports

None.

This is a documentation slice only.

## Initial Test Plan

- verify the command reference names the runtime sandbox repo explicitly
- verify the command reference includes the backend seeding and smoke startup commands
- verify the command reference keeps the Android client validation commands concise
- verify the command reference does not duplicate the full runbook

## Scenario Definition

Given a developer wants to smoke test the Android app on an emulator, the command reference should tell them which runtime sandbox commands to run first and which client-side validation steps to perform next.

Given the backend simulation is needed for a repeatable smoke test, the command reference should reduce ambiguity around the sandbox setup and the order of operations.

## Done Criteria

- the emulator smoke-test command reference is documented in the repository
- the reference explicitly names the runtime sandbox commands
- the repository documents the manual smoke-test sequence in executable terms
- no runtime app behavior changes are introduced
