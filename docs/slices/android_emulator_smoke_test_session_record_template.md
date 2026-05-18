# Android Emulator Smoke Test Session Record Template

- pack: android_compose_client
- runtime_targets:
  - android/app
  - external-api/axiom-exp-contacts
  - runtime-sandbox
- architecture_mode: layered_android_client

## Discovery Scope

Refine the emulator smoke-test workflow so each manual session has a compact record template:

- keep the Android client behavior unchanged
- keep the backend-readiness preflight and session classification intact
- record the backend command or seed source, the readiness result, and the observed Android client behavior in one place
- avoid adding backend ownership to this repository

This slice does not change app code. It makes the smoke-test record easier to interpret later and reduces ambiguity between backend setup and Android client behavior.

## Use-Case Contract

The exposure workflow should continue to support the existing Android client and emulator smoke-test flow:

- launch the app on an emulator or local device
- confirm backend readiness through the sandbox-backed preflight
- record the backend command or seed source used for the session
- classify the session as backend-ready or backend-not-ready
- record the Android client observations

## Record Shape

The session record should stay compact and consistently ordered:

```text
session date
backend command or seed source
backend readiness classification
Android client observations
follow-up notes
```

The record should make it obvious whether the backend was ready before any client observation is interpreted.

## Main Business Rules

- every smoke-test session should record the backend command or seed source used
- every smoke-test session should record the backend-ready or backend-not-ready classification
- app observations should be recorded only after the session is classified
- backend-not-ready sessions should be recorded as setup issues, not Android client regressions
- the repository does not own the runtime sandbox implementation
- the record format should stay short enough to use during a manual smoke test

## Required Ports

- no new app port is required
- the exposure workflow relies on the existing runtime sandbox and Android client boundaries

## Initial Test Plan

- verify the runbook or exposure guidance includes a compact session record template
- verify the template captures backend command provenance and readiness classification
- verify the template keeps backend setup failures separate from app observations
- verify the template keeps the record shape short and reusable
- verify the existing smoke-test guidance remains intact

## Scenario Definition

Given an Android emulator smoke-test session, the operator should record which backend command or seed source was used, whether the backend was ready, and what the Android client observed. If the backend was not ready, the record should still capture that as a setup issue rather than a client regression.

## Done Criteria

- the exposure workflow includes a compact session record template
- deterministic guidance distinguishes backend provenance, readiness, and app observations
- the existing emulator smoke-test guidance remains intact
- the repository does not take ownership of the runtime sandbox
