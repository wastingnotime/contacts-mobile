# Implementation: Route Mobile Contacts Traffic Through A Go BFF

## Summary

Shifted the Android client transport seam from a direct contacts API shape to a BFF-facing boundary while keeping the existing app behavior intact.

## What Changed

- Added BFF-named transport types:
  - `ContactsBffClient`
  - `HttpContactsBffClient`
  - `ContactsBffBaseUrlConfiguration`
  - `ContactsBffBaseUrlResolver`
- Updated `DefaultContactsRepository` and `MainActivity` to use the BFF seam explicitly
- Added BFF build config fields so the app can resolve the BFF base URL and BFF auth headers from Gradle properties
- Updated the BFF base URL resolver test to validate the new BFF-specific configuration messages
- Removed the temporary `ContactsApi*` compatibility aliases and renamed the affected tests so the codebase now uses the BFF vocabulary consistently
- Renamed the auth configuration and headers types and test file to the BFF naming convention

## Validation

- `./gradlew test`

The test suite passed after the BFF boundary refactor.
