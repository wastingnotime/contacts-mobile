# Android Emulator Backend Readiness Preflight

- pack: android_compose_client
- runtime_targets:
  - android/app
  - external-api/axiom-exp-contacts
  - runtime-sandbox
- architecture_mode: layered_android_client

## Discovery Scope

Refine the emulator exposure workflow so manual smoke tests begin with an explicit backend-readiness preflight:

- keep the Android client behavior unchanged
- keep the emulator smoke-test runbook intact
- require a visible check that the contacts backend is reachable and seeded before interpreting app behavior
- avoid adding backend ownership to this repository

This slice does not change the app code. It makes the exposure step clearer so emulator failures are not misread as client regressions when the backend simulation is unavailable.

## Use-Case Contract

The exposure workflow should continue to support the existing Android client and emulator smoke-test flow:

- launch the app on an emulator or local device
- load contacts from the configured backend
- exercise read and write flows against the backend
- record whether failures come from the app or from the backend simulation

## Main Business Rules

- the backend simulation must be verified before smoke-testing the app
- an unreachable or unseeded backend should be recorded as an exposure setup issue, not a client behavior failure
- navigation or viewport observations are only meaningful after backend readiness is confirmed
- this repository does not own the runtime sandbox implementation

## Required Ports

- no new app port is required
- the exposure workflow relies on the existing runtime sandbox and Android client boundaries

## Initial Test Plan

- verify the smoke-test runbook begins with a backend-readiness check
- verify the emulator session is not treated as a client failure when the backend simulation is unreachable
- verify the backend readiness result is recorded before app behavior is judged
- verify the existing emulator smoke-test guidance remains intact

## Scenario Definition

Given an Android emulator smoke-test session, the backend simulation should be confirmed ready and seeded before the app’s read/save behavior is interpreted. If the backend is unavailable, the session should be classified as a setup problem rather than an Android client regression.

## Done Criteria

- the exposure workflow includes an explicit backend-readiness preflight
- deterministic guidance distinguishes backend readiness failures from app regressions
- the existing emulator smoke-test guidance remains intact
- the repository does not take ownership of the runtime sandbox
