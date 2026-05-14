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

In `../runtime-sandbox`, start a seeded backend smoke path before you launch the Android app.

Recommended sequence:

1. Open `../runtime-sandbox`
2. Seed the backend with the representative contacts scenario
3. Start the backend smoke stack and leave the backend services running
4. Confirm the backend health endpoint is reachable before launching the emulator app

If you need the exact command sequence, use the runtime sandbox documentation and scripts for the backend smoke path. This Android repository intentionally does not own that backend simulation setup.

## Android Client Smoke Test

1. Build and install the Android client on the emulator.
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
