# Implementation: Centralize Final BFF Bootstrap Assembly

## Summary

Moved the final `ContactsBffBootstrap` wrapping step into one explicit interface-layer assembly object so the bootstrapper now hands off a view-model factory instead of constructing the wrapper inline.

## What Changed

- Added `ContactsBffBootstrapAssembly` to wrap a `ContactsViewModelFactory` in the final bootstrap object
- Added `ContactsBffViewModelFactoryAssembly` to keep the view-model factory construction explicit and reusable
- Updated `ContactsBffBootstrapper.build(configuration)` to consume the dedicated final-bootstrap assembly
- Kept the build-value, normalization, dependency, use-case, and factory seams unchanged
- Added deterministic tests for the view-model factory assembly and final bootstrap assembly

## Validation

- `./gradlew test`

The test suite passed after the final-bootstrap extraction.
