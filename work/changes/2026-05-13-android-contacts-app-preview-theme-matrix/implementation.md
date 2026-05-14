# Android Contacts App Preview Theme Matrix Implementation

## Slice

`docs/slices/android_contacts_app_preview_theme_matrix.md`

## Implemented

- added an explicit app-shell theme wrapper that uses Material 3 light and dark color schemes for previews
- expanded the app-shell preview surface to show the main contacts states in both light and dark variants
- kept the runtime `ContactsApp(viewModel)` path unchanged in behavior
- kept preview inputs local and deterministic, with no dependency on repository or network data

## Validation

- `./gradlew test` passed
- `git diff --check` passed

## Notes

- the preview matrix remains centered on `ContactsApp` because it is the closest stable design-time entrypoint to `MainActivity`
- the runtime application still uses the existing shell composition and does not depend on preview-only helpers
