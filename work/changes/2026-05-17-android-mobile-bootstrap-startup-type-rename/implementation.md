# Implementation: Rename Startup-Layer Bootstrap Types

## Summary

Renamed the startup-layer configuration and dependency types to app-level names so the startup path no longer uses BFF-specific vocabulary for objects that only model app bootstrap wiring.

## What Changed

- Renamed `ContactsBffBootstrapConfiguration` to `ContactsBootstrapConfiguration`
- Renamed `ContactsBffBootstrapBuildConfiguration` to `ContactsBootstrapBuildConfiguration`
- Renamed `ContactsBffBootstrapDependencies` to `ContactsBootstrapDependencies`
- Renamed the corresponding resolvers and build-configuration source to app-level names
- Updated the bootstrapper and startup tests to use the new names
- Kept the transport-layer BFF client, auth, API surface, and repository unchanged

## Validation

- `./gradlew test`

The test suite passed after the startup-layer rename.
