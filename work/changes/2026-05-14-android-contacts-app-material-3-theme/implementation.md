# Android Contacts App Material 3 Theme

## Implemented Behavior

- The app now uses a shared `ContactsTheme` entrypoint for runtime UI.
- The theme resolves light and dark static color schemes deterministically when dynamic color is disabled.
- Supported Android versions can still use dynamic color through the same theme entrypoint.
- Compose previews now render through the same app theme wrapper instead of a bare `MaterialTheme`.

## Code Changes

- Added `app/src/main/java/org/wastingnotime/contactsmobile/interfaces/theme/ContactsTheme.kt`
- Updated `app/src/main/java/org/wastingnotime/contactsmobile/interfaces/ContactsApp.kt`
- Updated `app/src/main/java/org/wastingnotime/contactsmobile/interfaces/ui/ContactsScreen.kt`
- Added `app/src/test/java/org/wastingnotime/contactsmobile/interfaces/theme/ContactsThemeTest.kt`

## Validation

- `./gradlew test`

