# Implementation: Keep One App Bootstrap Entry Point

## Summary

Removed the unused configuration-taking overload from `ContactsAppStart` so the Android entry point now has one explicit startup entry.

## What Changed

- Kept only `ContactsAppStart.bootstrap()`
- Removed the unused `bootstrap(configuration)` overload
- Updated the app-start test to exercise the single startup entry
- Kept `MainActivity` on the same one-entry app-start path

## Validation

- `./gradlew test`

The test suite passed after reducing the app-start facade to one entry point.
