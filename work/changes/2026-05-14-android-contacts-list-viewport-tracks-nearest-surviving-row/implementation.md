# Implementation: Android Contacts List Viewport Tracks Nearest Surviving Row

## What Changed

- extended `ContactsListViewportState` with a secondary visible-row anchor
- added a pure resolver for restoring the viewport when the anchor contact disappears
- updated `ContactsScreen` to capture the first two visible contact ids while scrolling
- updated `ContactsScreen` to restore the list to the nearest surviving row after list changes
- updated `ContactsViewModel` to store the richer viewport state
- added deterministic unit tests for secondary-anchor fallback and clamped fallback behavior

## Validation

- `./gradlew test`
- `git diff --check`

## Result

The contacts list now keeps the user near the same visible neighborhood even when the anchored contact disappears after create, update, delete, or refresh transitions.
