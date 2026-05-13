# Android Contacts Request Claims Headers Implementation

## Slice

`docs/slices/android_contacts_request_claims_headers.md`

## Implemented

- added build-time configuration for contacts API request claims subject and roles
- added a request-claims configuration/resolution seam in the infrastructure config layer
- updated the Android entry point to compose the contacts API client with explicit claims headers
- extended the HTTP contacts client to apply `x-auth-subject` and `x-auth-roles` on every request
- kept the existing contacts list and contact detail flows unchanged apart from the request boundary
- added deterministic tests for auth claims resolution and HTTP header injection on list and detail requests

## Validation

- `./gradlew test` passed
- the Android Gradle build still targets API 35 and uses the configured local SDK

## Notes

- default local claims remain `admin-user` and `admin`
- the slice does not add login or session handling
- the transport boundary now makes the claims requirement explicit without changing UI behavior
