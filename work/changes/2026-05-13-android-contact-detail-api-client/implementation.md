# Android Contact Detail API Client Implementation

## Slice

`docs/slices/android_contact_detail_bff_contract.md`

## Implemented

- added a detail use case, `LoadContactById`, for the selected contact resource
- extended the repository port and HTTP client to support `GET /api/contacts/{id}`
- added single-contact JSON parsing for snake_case transport payloads
- replaced the local-only detail view with an API-backed detail state machine
- kept the contacts list slice intact while allowing refresh to reload the active detail view
- added deterministic tests for detail use-case behavior, repository mapping, view-model navigation, and single-contact parsing

## Validation

- `./gradlew test` passed
- the Android Gradle build still targets API 35 and uses the configured local SDK

## Notes

- `GET /api/contacts/{id}` returns a `NotFound` detail state when the BFF does not return a contact
- the existing build-time API base-url selection remains unchanged
- detail loading and retry are handled in the view model without introducing a navigation library
