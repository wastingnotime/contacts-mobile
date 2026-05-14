# Android Emulator Smoke Test Runbook

Use this runbook when manually validating the Android contacts client on an emulator.

## Purpose

Keep backend simulation explicit and separate from the Android client repo.

The emulator smoke-test backend should come from `../runtime-sandbox`.

## Prerequisites

- Android Studio or an installed Android SDK
- an Android emulator configured with the app's emulator base URL
- the adjacent runtime sandbox repo at `../runtime-sandbox`

## Backend Simulation

Use the runtime sandbox commands below before launching the Android app:

```bash
cd ../runtime-sandbox
make backend-smoke BACKEND_SMOKE_SCENARIO=representative-directory
```

That command seeds the backend, starts the backend services, and leaves them running for follow-on smoke work.

If you need only the seed step, the sandbox also exposes:

```bash
cd ../runtime-sandbox
make backend-seeded SEED_SCENARIO=representative-directory
```

This Android repository intentionally does not own the backend simulation setup.

## Android Client Smoke Test

1. Build and install the Android client on the emulator.

```bash
./gradlew installDebug
```

2. Verify the app resolves the emulator base URL configured for contacts API access.
3. Confirm the contacts list loads from the backend sandbox data.
4. Tap a contact and confirm the detail screen opens.
5. Trigger a refresh and verify transient failures preserve already-loaded content.
6. Verify retry returns the app to loading and then refreshed content.

## What To Look For

- visible loading states
- visible empty states
- visible error states
- stale-data preservation on transient failure
- retry behavior that returns to loading

## Notes

- do not treat the Android client repo as the backend simulation owner
- use `../runtime-sandbox` for deterministic backend setup and smoke checks
- keep emulator smoke tests separate from release acceptance and expectation-gap review
