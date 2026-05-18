# Android Client Bootstrap Implementation

## Slice

`docs/slices/android_contacts_list_bff_client.md`

## Implemented

- added an `android_compose_client` pack and updated the repository docs to match
- scaffolded an Android Gradle app module under `app/`
- added a configurable HTTP client for `GET /api/contacts`
- added build-time contacts BFF base-url selection for emulator, local device, and production targets
- raised the Android module to compile and target API 35 to stay aligned with Android 15 Play policy requirements
- added transport parsing for the backend's snake_case contact payloads
- added a load use case that returns empty or loaded states
- added a Compose screen with loading, empty, error, list, and contact detail states
- added deterministic unit tests for parsing, repository mapping, load behavior, view-model selection state transitions, and API base-url resolution

## Validation

- `./gradlew test` passed
- the Android Gradle wrapper is checked in and uses Gradle 8.8
- the build uses the local SDK at `/home/henrique/tools/android-sdk`

## Notes

- the Android app defaults to `http://10.0.2.2:8010` for emulator access
- the `contactsApiEnvironment` Gradle property can select `emulator`, `local_device`, or `production`
- the Android module now compiles and targets API 35
- the backend contract is consumed directly at `/api/contacts`
- future slices can add authenticated headers, deeper contact actions, or edit flows without changing the base client shape
