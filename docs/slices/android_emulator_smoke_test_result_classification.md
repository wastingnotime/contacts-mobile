# Android Emulator Smoke Test Result Classification

- pack: android_compose_client
- runtime_targets:
  - android/app
  - external-api/axiom-exp-contacts
  - runtime-sandbox
- architecture_mode: layered_android_client

## Discovery Scope

Refine the emulator smoke-test workflow so every manual session records a simple result classification before interpreting Android client behavior:

- keep the Android client behavior unchanged
- keep the emulator smoke-test runbook and backend-readiness preflight intact
- classify the session as backend-ready or backend-not-ready before judging app observations
- avoid adding backend ownership to this repository

This slice does not change product code. It makes the smoke-test record itself more explicit so backend setup problems and Android client behavior are not mixed together.

## Use-Case Contract

The exposure workflow should continue to support the existing Android client and emulator smoke-test flow:

- launch the app on an emulator or local device
- confirm backend readiness through the sandbox-backed preflight
- load contacts from the configured backend
- exercise read and write flows against the backend
- record whether the session was backend-ready before interpreting app behavior

## Main Business Rules

- every emulator smoke-test session should have an explicit backend-ready or backend-not-ready classification
- app behavior should only be judged after backend-ready is recorded
- backend-not-ready should be recorded as a setup issue rather than a client regression
- navigation, viewport, and refresh observations are only meaningful once the session is backend-ready
- this repository does not own the runtime sandbox implementation

## Required Ports

- no new app port is required
- the exposure workflow relies on the existing runtime sandbox and Android client boundaries

## Initial Test Plan

- verify the smoke-test guidance includes a session result classification step
- verify backend-not-ready sessions are recorded separately from Android client observations
- verify backend-ready sessions continue into app validation
- verify the existing backend-readiness and emulator smoke-test guidance remains intact

## Scenario Definition

Given an Android emulator smoke-test session, the operator should record whether backend readiness succeeded before interpreting the Android client’s behavior. If backend readiness fails, the session should be marked as setup failure instead of an app regression.

## Done Criteria

- the exposure workflow includes an explicit session classification
- deterministic guidance distinguishes backend-ready and backend-not-ready sessions
- the existing emulator smoke-test guidance remains intact
- the repository does not take ownership of the runtime sandbox
