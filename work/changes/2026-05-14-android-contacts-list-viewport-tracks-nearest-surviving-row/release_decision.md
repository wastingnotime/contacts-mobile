# Release Decision: Android Contacts List Viewport Tracks Nearest Surviving Row

## Decision

Accept.

## Basis

- The slice is coherent and makes the viewport recovery rule explicit for list mutations.
- The implementation preserves the nearest surviving row as the scroll anchor.
- The slice keeps the list behavior deterministic across content changes.
- `./gradlew test` and `git diff --check` passed.

## Outcome

The Android contacts list viewport nearest-surviving-row slice is accepted as the internal released version.
