# Android Contacts List Viewport Continuity Guidance EGD

## Intent

Android contacts list viewport continuity guidance as one intent bundle.

## Constituents

- `docs/slices/android_contacts_list_scroll_position_persists_across_navigation.md`
- `docs/slices/android_contacts_list_viewport_survives_list_content_changes.md`
- `docs/slices/android_contacts_list_viewport_tracks_nearest_surviving_row.md`

## Evidence Reviewed

- `docs/slices/android_contacts_list_scroll_position_persists_across_navigation.md`
- `docs/slices/android_contacts_list_viewport_survives_list_content_changes.md`
- `docs/slices/android_contacts_list_viewport_tracks_nearest_surviving_row.md`
- `work/changes/2026-05-14-android-contacts-list-scroll-position-persists-across-navigation/implementation.md`
- `work/changes/2026-05-14-android-contacts-list-viewport-survives-list-content-changes/implementation.md`
- `work/changes/2026-05-14-android-contacts-list-viewport-tracks-nearest-surviving-row/implementation.md`
- `./gradlew test`

## Summary

The list viewport continuity guidance is coherent as one intent bundle. It defines how the Android contacts list preserves the user’s place across navigation and list mutations, including fallback when the previously anchored row disappears.

The bundle stays local to presentation state and list composition and does not change the backend contract.

## Findings

### 1. The constituent slices describe one viewport continuity intent

Observed behavior:

- one slice preserves scroll position across navigation
- one slice preserves the visible list area across list content changes
- one slice handles the fallback when the anchor row disappears

Assessment:

- these are not separate product intents
- they are one list viewport continuity intent

### 2. The intent is operationally complete enough for release

Observed behavior:

- the viewport survives opening detail and form surfaces
- the viewport survives create, update, delete, and refresh transitions when possible
- the viewport falls back to the nearest surviving row when the anchor disappears

Assessment:

- no blocking expectation gap remains for viewport continuity
- the list keeps the user near the same visible neighborhood across the supported transitions

### 3. No backend contract risk is introduced

Observed behavior:

- the viewport behavior is implemented in the presentation layer
- no new endpoint or transport rule is introduced

Assessment:

- the bundle preserves the existing backend contract

## Recommendation

Continue to release.

Reasoning:

- the constituent slices are one coherent list viewport intent
- the implementation evidence supports the intended viewport behavior
- the bundle remains within existing contacts client boundaries
