# Implementation: Centralize Mobile BFF Bootstrap

## Summary

Moved the BFF dependency assembly out of `MainActivity` and into a dedicated bootstrap object so the interface layer stays thin and the startup wiring has one explicit home.

## What Changed

- Added `ContactsBffBootstrapConfiguration` to hold the startup values needed for BFF assembly
- Added `ContactsBffBootstrapper.build(configuration)` to assemble the BFF base URL, auth headers, API surface, client, repository, and view-model factory
- Kept a default `build()` overload that reads from `BuildConfig` for the actual app startup path
- Simplified `MainActivity` to call the bootstrapper and start the UI
- Added a deterministic bootstrap test for valid configuration
- Added a deterministic bootstrap test for blank API-prefix validation

## Validation

- `./gradlew test`

The test suite passed after the bootstrap refactor.
