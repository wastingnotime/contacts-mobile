# Android Emulator Smoke Test Runbook Impact Analysis

## Slice Intent

Document a clear emulator smoke-test runbook for the Android contacts client, with backend simulation sourced from `../runtime-sandbox`.

This is a documentation and exposure refinement, not a product behavior change.

## Why This Slice Now

The client has already been exposed on an emulator and the resulting feedback showed that the backend simulation path needs to be explicit. The next useful refinement is to document the repeatable smoke-test sequence in a single place so future manual validation does not depend on ad hoc memory.

The pressure is around exposure clarity:

- the emulator smoke-test path should be repeatable
- the backend simulation source should be named explicitly
- the client repo should not be mistaken for the backend simulation owner

## Impacted Boundaries

### Interfaces

- the operator-facing documentation in this repo needs a clear emulator smoke-test runbook

### Application

- no application use case changes are expected

### Infrastructure

- no infrastructure changes are expected in the Android client repo

### Tests

- no new business tests are expected
- the documentation should still be concrete enough to support manual validation

## Model Pressure

The current model already distinguishes runtime behavior from exposure.

This slice pressures the exposure workflow itself: manual emulator validation should have a documented sequence, and that sequence should explicitly use the runtime sandbox for backend simulation.

## Build Guidance

Keep the slice narrow and documentary.

It should not:

- add backend simulation code to the Android client repo
- change the client API contract
- introduce new automation that depends on live network assumptions
