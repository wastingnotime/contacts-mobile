# Android Emulator Smoke Test Failure Interpretation Matrix

- pack: android_compose_client
- runtime_targets:
  - android/app
- architecture_mode: layered_android_client

## Discovery Scope

Add a failure-interpretation matrix to the emulator smoke-test guidance so the operator can distinguish backend availability issues, client startup issues, and expected transient failure states.

This slice does not change app behavior. It makes the smoke-test runbook easier to interpret by documenting common failure signatures and what they usually imply.

The failure matrix should cover:

- backend smoke path not running or not seeded
- emulator app not installing or not launching
- contacts API base URL misconfiguration
- expected transient refresh failure behavior
- contact detail not found behavior

## Use-Case Contract

No new business use case is introduced.

This slice documents manual validation interpretation. It does not change runtime behavior.

## Main Business Rules

- failure signatures should be concrete and observable
- the matrix should be tied to the emulator smoke-test runbook
- the matrix should keep `../runtime-sandbox` as the backend simulation source
- the matrix should help avoid misclassifying environment issues as client regressions
- the matrix should stay diagnostic rather than procedural

## Required Ports

None.

This is a documentation slice only.

## Initial Test Plan

- verify the matrix names the backend sandbox path explicitly
- verify the matrix separates environment failures from expected client states
- verify the matrix does not repeat the full smoke-test procedure
- verify no runtime app behavior changes are introduced

## Scenario Definition

Given a developer is running the emulator smoke test and something fails, the runbook should help them tell whether the problem is backend availability, emulator/app setup, base URL configuration, or an expected client state.

Given the client shows a transient or not-found state, the matrix should document what that state means and when it is a normal outcome rather than a regression.

## Done Criteria

- the emulator smoke-test failure-interpretation matrix is documented in the repository
- the failure signatures are explicit enough to reduce false regression reports
- the runbook remains tied to `../runtime-sandbox`
- no runtime app behavior changes are introduced
