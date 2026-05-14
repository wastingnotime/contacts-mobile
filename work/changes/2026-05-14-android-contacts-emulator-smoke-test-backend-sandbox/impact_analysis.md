# Android Contacts Emulator Smoke Test Backend Sandbox Impact Analysis

## Slice Intent

Make the emulator smoke-test path explicit by documenting that backend simulation comes from `../runtime-sandbox`.

This is a workflow and exposure refinement, not a product behavior change.

## Why This Slice Now

The released stale-data slice was exposed on an emulator, and the resulting feedback showed a failure state because the backend was not providing data. The next useful refinement is to remove ambiguity about where the backend simulation should come from when validating the Android client manually.

The pressure is around exposure clarity:

- emulator smoke tests should have a defined backend simulation source
- the client repo should not be expected to simulate the backend itself
- the operator guidance should point to the correct adjacent repo

## Impacted Boundaries

### Interfaces

- exposure notes and living evidence need an explicit backend simulation reference

### Application

- no application use case changes are expected

### Infrastructure

- no infrastructure changes are expected in the Android client repo

### Tests

- no new business tests are expected
- the change is documentary, but it should still be precise enough to support future manual validation

## Model Pressure

The current model already distinguishes runtime behavior from exposure.

This slice pressures the exposure workflow itself: manual emulator validation should have a concrete backend simulation source, and that source lives in the sandbox repo rather than this client repo.

## Build Guidance

Keep the slice narrow and documentary.

It should not:

- add runtime backend simulation code to the Android client
- change the client API contract
- introduce new testing infrastructure in this repository
