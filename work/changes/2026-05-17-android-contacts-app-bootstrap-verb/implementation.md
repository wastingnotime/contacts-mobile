# Implementation: Rename The App Bootstrap Verb

## Summary

Renamed the app-start facade method from `start()` to `bootstrap()` so the Android entry point uses an explicit bootstrap verb instead of a generic startup verb.

## What Changed

- Renamed the app-start entry point to `bootstrap(configuration)`
- Renamed the default app-start path to `bootstrap()`
- Updated `MainActivity` to call `ContactsAppStart.bootstrap()`
- Updated the app-start test to use the new verb
- Kept the startup behavior unchanged

## Validation

- `./gradlew test`

The test suite passed after the bootstrap-verb rename.
