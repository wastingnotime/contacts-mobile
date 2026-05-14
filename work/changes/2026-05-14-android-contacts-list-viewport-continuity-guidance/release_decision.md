# Release Decision: Android Contacts List Viewport Continuity Guidance

## Decision

Accept.

## Basis

- The intent bundle is coherent and centered on one viewport continuity workflow.
- The constituent slices cover navigation persistence, content-change stability, and nearest-surviving-row fallback.
- The guidance remains local, deterministic, and backend-contract preserving.
- `git diff --check` passes.

## Outcome

The Android contacts list viewport continuity intent is accepted as the internal released version.
