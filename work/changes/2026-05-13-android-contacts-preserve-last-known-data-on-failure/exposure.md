# Exposure: Android Contacts Preserve Last Known Data On Failure

## Released Slice

`docs/slices/android_contacts_preserve_last_known_data_on_failure.md`

## Exposure Target

Expose the released slice in two concrete ways:

- Android Studio design-time preview through `ContactsApp`
- runtime install on an Android emulator or local device pointed at the configured contacts API base URL

## Exposure Plan

- use the `ContactsApp` preview to review the main list and detail states during UI iteration
- install the app on an emulator using the configured emulator base URL
- verify that a transient refresh failure preserves visible contacts or detail content while surfacing the error banner
- verify that retry still returns the screen to loading and then to refreshed content

## Expected Feedback Channels

- Android Studio preview review during UI changes
- manual smoke testing on emulator or device
- any user-visible friction around stale-data banners, retry affordance, or detail refresh behavior

## Notes

- this exposure step does not add new persistence, telemetry, or operations ownership
- the slice remains exposed through the existing build-time environment selection and API client configuration
