# Implementation: Centralize BFF Build Configuration Sourcing

## Summary

Moved raw `BuildConfig` reads out of `ContactsBffBootstrapper` and into a dedicated startup-configuration source so app startup now has a clear two-step path: read build values once, then normalize them once before assembling the BFF bootstrap.

## What Changed

- Added `ContactsBffBootstrapBuildConfigurationSource` to read raw BFF startup values from `BuildConfig`
- Updated `ContactsBffBootstrapper.build()` to consume the source object instead of naming `BuildConfig` fields directly
- Kept `ContactsBffBootstrapConfigurationResolver` unchanged as the normalization seam
- Added a deterministic test for the build-configuration source mapping

## Validation

- `./gradlew test`

The test suite passed after the startup-source extraction.
