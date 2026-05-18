# Android Emulator Backend Readiness Preflight Impact Analysis

## Slice Intent

Keep the emulator smoke-test flow unchanged while making backend readiness a strict gate before the Android app is installed, launched, or judged.

The repository already documents backend simulation in `../runtime-sandbox`. The remaining pressure is to keep readiness explicit and early enough that backend setup problems are not mixed with client observations.

## Why This Slice Now

The smoke-test path already has a runbook, a command reference, and session recording guidance. What still needs tightening is the order of operations: the backend should be confirmed reachable and seeded before any Android client behavior enters the record.

This slice stays intentionally narrow and leaves provenance capture to the separate session record template.

## Impacted Boundaries

### Exposure Workflow

- keep readiness as the first step in the manual smoke-test path
- keep readiness separate from client observation recording
- keep the backend-not-ready outcome classified as setup failure

### Documentation

- update the backend-readiness slice to state the preflight happens before app install or launch
- keep the runbook as the source of the detailed procedure
- do not duplicate the session record template here

### Runtime Sandbox Boundary

- preserve the explicit dependency on `../runtime-sandbox`
- do not imply ownership of backend simulation in this repository

### Android Client

- do not change app code
- do not change smoke-test runtime behavior

## Model Pressure

The main pressure is sequencing, not behavior.

The repository needs a hard boundary that says:

- first confirm the backend is ready and seeded
- then install or launch the app
- then record and interpret client behavior

That keeps emulator setup failures from being misread as regressions in the Android client.

## Build Guidance

If this slice is built later, the implementation should:

- keep the readiness gate visible near the top of the runbook
- mention `../runtime-sandbox` explicitly
- avoid expanding into a detailed record template
- avoid creating backend-ownership language in this repository
