# Android Contacts App Preview State Matrix Implementation

## Slice

`docs/slices/android_contacts_app_preview_state_matrix.md`

## Implemented

- split the app shell composition out into `ContactsAppContent` so previews can render the shell deterministically without a live view model
- added design-time previews for the app shell covering loading, empty, loaded, loaded-with-refresh-warning, detail, and detail-with-refresh-warning states
- kept the runtime `ContactsApp(viewModel)` entry unchanged in behavior
- kept the preview data local and deterministic, with no repository or network dependency

## Validation

- `./gradlew test` passed
- `git diff --check` passed

## Notes

- existing screen-level previews remain available as a lower-level inspection aid
- the new preview matrix is centered on the app shell because that is the closest stable design-time entrypoint to `MainActivity`
