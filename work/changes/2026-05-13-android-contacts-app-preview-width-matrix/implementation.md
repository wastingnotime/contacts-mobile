# Android Contacts App Preview Width Matrix Implementation

## Slice

`docs/slices/android_contacts_app_preview_width_matrix.md`

## Implemented

- expanded the app-shell preview surface to show loaded list and loaded detail states at compact and expanded widths
- kept the runtime `ContactsApp(viewModel)` path unchanged in behavior
- kept preview inputs local and deterministic, with no dependency on repository or network data
- preserved the existing light and dark theme preview coverage while adding width-specific variants

## Validation

- `./gradlew test` passed
- `git diff --check` passed

## Notes

- the width previews are centered on `ContactsApp` because it is the closest stable design-time entrypoint to `MainActivity`
- this slice is about visual inspection in Android Studio, not runtime layout branching
