# Implementation: Android Contacts Edit Contact API Client

## Summary

Added a backend-backed edit-contact flow to the Android client.

## Changes

- Added `UpdateContact` and `UpdateContactCommand` in `app/src/main/java/org/wastingnotime/contactsmobile/application/UpdateContact.kt`
- Extended `ContactsRepository` and `ContactsBffClient` with update-contact support
- Implemented `PUT /api/contacts/{id}` in `HttpContactsBffClient`
- Added edit-contact form and success state handling to the app UI
- Wired the edit flow through `ContactsViewModel`, `ContactsApp`, and `MainActivity`
- Added tests for update use-case behavior, repository mapping, HTTP request/response handling, and view-model edit flow

## Validation

- `./gradlew test` passed
- `git diff --check` will be run before commit
