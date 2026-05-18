# Release Decision: Android Contacts List Scroll Position Persists Across Navigation

## Decision

Accept.

## Basis

- The slice is coherent and keeps the list viewport state local and explicit.
- The implementation preserves scroll position across detail and form navigation without changing contacts behavior.
- The slice adds deterministic viewport-retention tests.
- `./gradlew test` and `git diff --check` passed.

## Outcome

The Android contacts list scroll-position persistence slice is accepted as the internal released version.
