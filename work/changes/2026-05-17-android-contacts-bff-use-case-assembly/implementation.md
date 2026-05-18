# Implementation: Centralize BFF Use-Case Assembly

## Summary

Moved the five contacts application use-case constructions into one explicit interface-layer assembly object so the bootstrapper now focuses on combining the assembled use cases into the final UI factory.

## What Changed

- Added `ContactsBffUseCases` to group the five contacts use cases
- Added `ContactsBffUseCaseAssembly` to construct the use cases from a repository in one place
- Updated `ContactsBffBootstrapper.build(configuration)` to consume the assembled use cases
- Kept the resolved BFF dependency object and startup normalization seams unchanged
- Added a deterministic test for the use-case assembly

## Validation

- `./gradlew test`

The test suite passed after the use-case assembly extraction.
