# Implementation: Centralize The Android App Start Facade

## Summary

Added a thin `ContactsBffAppStart` facade so `MainActivity` now depends on one named startup object instead of the bootstrapper type directly.

## What Changed

- Added `ContactsBffAppStart` as the explicit Android app-start facade
- Kept the default app-start path delegating to the existing bootstrapper
- Added an explicit overload for starting from a provided bootstrap configuration
- Updated `MainActivity` to call the app-start facade
- Added a deterministic test for explicit app-start delegation

## Validation

- `./gradlew test`

The test suite passed after the app-start facade extraction.
