# Exposure: Android Contacts MVP

## Released State

The accepted internal release is the BFF-backed Android contacts client with:

- list, detail, create, edit, and delete flows
- request claims at the BFF boundary
- shared Material 3 theme support
- create/edit/delete navigation refinements
- emulator access and smoke-test guidance

## Exposure Target

Expose the released state in the following concrete contexts:

- Android Studio previews for runtime UI inspection
- runtime installation on an Android emulator or local device
- manual smoke testing against the configured contacts BFF environment
- manual inspection of the create/edit/delete navigation policies in the running app

## Exposure Plan

- use Android Studio previews to review the shared theme and core screens during UI iteration
- install the app on an emulator or local device using the configured BFF base URL and request-claims settings
- use `../runtime-sandbox` as the backend simulation source for emulator smoke tests
- validate that list, detail, create, edit, and delete flows still behave as released
- observe any friction around the BFF boundary, navigation policies, or visual consistency

## Expected Feedback Channels

- Android Studio preview review during UI iteration
- manual smoke testing on emulator or local device
- direct feedback from anyone exercising the released MVP in a real device session
- notes about mismatches between the BFF contract, the navigation policy, and the rendered UI

## Notes

- this exposure step does not add new persistence, telemetry, or operations ownership
- backend simulation setup remains the responsibility of the adjacent runtime sandbox workflow
- the exposure record captures how the accepted release is put into contact with reality, not a new product requirement
