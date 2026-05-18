# Android Emulator Smoke Test Command Reference Impact Analysis

## Slice Intent

Keep the emulator smoke-test runbook as the source of truth while adding a thin command index that names the sandbox and client commands explicitly.

The repository already has a detailed smoke-test runbook. The remaining pressure is to give operators a short command reference they can scan without re-reading the full procedure.

## Why This Slice Now

The current command-reference slice overlaps the runbook too much. The better refinement is to keep it focused on commands and ordering cues:

- which sandbox command seeds and starts backend simulation
- which Android client command installs the app
- which observation steps happen after backend readiness

That keeps the reference useful without turning it into a second runbook.

## Impacted Boundaries

### Documentation

- add a concise command index for the smoke-test path
- keep the detailed runbook as the primary procedural document

### Exposure Workflow

- preserve the backend-first ordering
- preserve the `../runtime-sandbox` dependency
- keep the Android validation command sequence short and explicit

### Runtime Sandbox Boundary

- continue to point at sandbox commands without taking ownership of sandbox behavior

### Android Client

- do not change app code
- do not change smoke-test runtime behavior

## Model Pressure

The pressure is discoverability of commands, not new behavior.

Operators should be able to see, at a glance:

- what to run in the sandbox
- what to run in the Android repo
- in which order to run them

The slice should not repeat the whole runbook, the readiness preflight, or the session record template.

## Build Guidance

If this slice is built later, the implementation should:

- keep the content short and command-focused
- avoid duplicating outcome or failure matrices
- avoid duplicating the session record template
- keep the runbook as the detailed source of truth
