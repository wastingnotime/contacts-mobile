# Release Decision: Android Contacts Stale Data Guidance

## Decision

Accept.

## Basis

- The intent bundle is coherent and centered on one stale-data workflow.
- The constituent slices cover stale visibility and dismissal on preserved list and detail content.
- The guidance remains local, deterministic, and backend-contract preserving.
- `git diff --check` passes.

## Outcome

The Android contacts stale-data intent is accepted as the internal released version.
