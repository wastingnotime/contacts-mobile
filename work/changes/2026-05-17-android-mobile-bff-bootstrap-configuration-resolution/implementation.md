# Implementation: Resolve BFF Bootstrap Configuration At Startup

## Summary

Moved the raw `BuildConfig` mapping out of `ContactsBffBootstrapper` and into a dedicated startup configuration resolver so the interface-layer bootstrap path has one explicit seam for turning build values into bootstrap configuration.

## What Changed

- Added `ContactsBffBootstrapBuildConfiguration` to hold the raw build-time startup values
- Added `ContactsBffBootstrapConfigurationResolver` to normalize and validate those raw values
- Updated `ContactsBffBootstrapper.build()` to resolve the raw build-time configuration before assembling the BFF client and repository
- Kept `ContactsBffBootstrapper.build(configuration)` unchanged for explicit bootstrap assembly tests and future callers
- Added deterministic tests for valid configuration resolution and blank API-prefix failure

## Validation

- `./gradlew test`

The test suite passed after the startup-configuration resolver extraction.
