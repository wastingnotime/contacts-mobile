# Implementation: Android Contacts List Viewport Survives List Content Changes

## What Changed

- extended `ContactsListViewportState` with an anchor contact id
- kept the current viewport state in `ContactsViewModel`
- updated `ContactsScreen` to restore the list viewport against the current contact collection
- updated `ContactsScreen` to report the current visible contact anchor back to the view-model
- added deterministic unit tests for viewport behavior during create, update, and delete flows

## Validation

- `./gradlew test`
- `git diff --check`

## Result

The contacts list now keeps the same visible area when the list content changes, instead of snapping back to the top after CRUD transitions or refreshes when the anchor contact still exists.
