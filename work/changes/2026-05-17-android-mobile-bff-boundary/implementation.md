# Implementation: Route Mobile Contacts Traffic Through A Go BFF

## Summary

Shifted the Android client transport seam from a direct contacts API shape to a BFF-facing boundary while keeping the existing app behavior intact.

## What Changed

- Added BFF-named transport types:
  - `ContactsBffClient`
  - `HttpContactsBffClient`
  - `ContactsBffBaseUrlConfiguration`
  - `ContactsBffBaseUrlResolver`
- Kept compatibility aliases in the old `ContactsApi*` files so existing tests and call sites continue to compile during the transition
- Updated `DefaultContactsRepository` and `MainActivity` to use the BFF seam explicitly
- Added BFF build config fields alongside the existing API-named fields so the app can resolve the BFF base URL from Gradle properties
- Updated the BFF base URL resolver test to validate the new BFF-specific configuration messages

## Validation

- `./gradlew test`

The test suite passed after the BFF boundary refactor.
