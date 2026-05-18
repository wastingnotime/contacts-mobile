# Implementation: Rename The Android App Start Facade

## Summary

Renamed the startup facade from `ContactsBffAppStart` to `ContactsAppStart` so the entry-point vocabulary matches the app boundary instead of the transport boundary.

## What Changed

- Renamed the startup facade object to `ContactsAppStart`
- Renamed the corresponding startup test to match the new app-level boundary
- Updated `MainActivity` to call `ContactsAppStart`
- Kept the startup behavior unchanged

## Validation

- `./gradlew test`

The test suite passed after the startup facade rename.
