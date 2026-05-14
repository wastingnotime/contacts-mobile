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

## Backend Readiness Preflight

Before interpreting any Android client result, confirm the backend simulation is ready:

1. Run the sandbox smoke command.
2. Confirm the backend health endpoint becomes reachable.
3. Confirm the seeded contacts are available in the sandbox-backed backend.
4. Only then install and launch the Android app.

If the backend is not reachable or not seeded, stop the smoke test and record the failure as an exposure setup issue rather than an Android client regression.

## Session Result Classification

Record one of these outcomes before writing any app observation:

- `backend-ready`
- `backend-not-ready`

Use `backend-ready` only after the preflight succeeds and the Android app can be judged against the seeded backend.
Use `backend-not-ready` when the backend simulation is unavailable, unseeded, or otherwise unusable for app validation.

## Session Record Template

Record the smoke-test session in a compact form:

```text
backend command:
readiness:
session classification:
app observations:
```

Example:

```text
backend command: cd ../runtime-sandbox && make backend-smoke BACKEND_SMOKE_SCENARIO=representative-directory
readiness: backend ready
session classification: backend-ready
app observations: contacts loaded, detail opened, refresh preserved stale content
```

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

## Outcomes Matrix

| Step | Expected outcome |
| --- | --- |
| `make backend-smoke BACKEND_SMOKE_SCENARIO=representative-directory` in `../runtime-sandbox` | Backend services start, health becomes reachable, and seeded contacts are available. |
| `./gradlew installDebug` on the Android client repo | The debug app installs on the emulator without build or install errors. |
| App launch on the emulator | The contacts client opens and resolves the configured emulator base URL. |
| Initial contacts load | The list renders seeded contacts from the sandbox-backed backend. |
| Contact detail open | Tapping a contact shows the detail screen for that contact. |
| Transient refresh failure | Already-loaded contacts or detail remain visible and a refresh failure banner appears. |
| Retry after transient failure | The app returns to loading, then to refreshed content once the backend is available again. |

## Failure Interpretation Matrix

| Symptom | Likely meaning |
| --- | --- |
| Backend smoke command does not complete or health never becomes reachable | The runtime sandbox backend is not running, not seeded, or not healthy. |
| Backend readiness preflight fails before app install | Stop the smoke session and treat the problem as sandbox setup, not an Android client defect. |
| `./gradlew installDebug` fails | The Android build or install path is broken before smoke validation begins. |
| The app launches but cannot reach the contacts API | The emulator base URL is misconfigured or the backend is unavailable. |
| The app shows `Unable to load contacts` on first load | The backend is unavailable or the app cannot reach the configured API. |
| A refresh banner appears while content stays visible | This is an expected transient failure state, not a regression. |
| Detail shows `Contact not found` | This is an expected not-found state, not a refresh failure. |

## What To Look For

- visible loading states
- visible empty states
- visible error states
- stale-data preservation on transient failure
- retry behavior that returns to loading

## Notes

- do not treat the Android client repo as the backend simulation owner
- use `../runtime-sandbox` for deterministic backend setup and smoke checks
- confirm backend readiness before judging Android client behavior
- classify each smoke-test session before recording app observations
- keep emulator smoke tests separate from release acceptance and expectation-gap review
