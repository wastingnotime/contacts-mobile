# Implementation: Centralize BFF Resolved Dependencies

## Summary

Moved the resolved BFF client and repository assembly into one explicit interface-layer dependency object so the bootstrapper can focus on the final UI bootstrap step instead of constructing the transport graph inline.

## What Changed

- Added `ContactsBffBootstrapDependencies` to group the resolved BFF client and repository
- Added `ContactsBffBootstrapDependenciesResolver` to assemble that dependency object from validated bootstrap configuration
- Updated `ContactsBffBootstrapper.build(configuration)` to consume the resolved dependency object
- Kept the final view-model factory assembly unchanged
- Added a deterministic test for dependency resolution

## Validation

- `./gradlew test`

The test suite passed after the dependency-object extraction.
