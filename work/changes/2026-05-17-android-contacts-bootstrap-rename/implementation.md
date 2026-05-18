# Implementation: Rename The Final Bootstrap Object

## Summary

Renamed the final bootstrap object from `ContactsBffBootstrap` to `ContactsBootstrap` so the object exposed to the activity uses app-level vocabulary instead of BFF-specific vocabulary.

## What Changed

- Renamed the final bootstrap data object to `ContactsBootstrap`
- Renamed the final bootstrap assembly object to `ContactsBootstrapAssembly`
- Updated `ContactsBffBootstrapper` to return `ContactsBootstrap`
- Updated `ContactsAppStart` and `MainActivity` to use the app-level bootstrap type
- Renamed the assembly test to match the app-level boundary

## Validation

- `./gradlew test`

The test suite passed after the bootstrap type rename.
