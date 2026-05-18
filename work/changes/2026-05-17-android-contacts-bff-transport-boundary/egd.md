# Android Contacts BFF Transport Boundary EGD

## Scope

This review covers the current BFF transport boundary for the contacts client as a bundle:

- `docs/slices/android_contacts_request_claims_headers.md`
- `docs/slices/android_contacts_create_contact_api_client.md`
- `docs/slices/android_contacts_edit_contact_api_client.md`
- `docs/slices/android_contacts_delete_contact_api_client.md`

## Evidence Reviewed

- `docs/semantics/model_hypothesis.md`
- `docs/semantics/domain_background_knowledge.md`
- the slice docs listed above
- `work/changes/2026-05-13-android-contacts-request-claims-headers/implementation.md`
- `work/changes/2026-05-14-android-contacts-create-contact-api-client/implementation.md`
- `work/changes/2026-05-14-android-contacts-edit-contact-api-client/implementation.md`
- `work/changes/2026-05-14-android-contacts-delete-contact-api-client/implementation.md`
- `app/src/main/java/org/wastingnotime/contactsmobile/infrastructure/config/ContactsBffAuthConfiguration.kt`
- `app/src/main/java/org/wastingnotime/contactsmobile/infrastructure/http/HttpContactsBffClient.kt`
- `app/src/main/java/org/wastingnotime/contactsmobile/infrastructure/http/DefaultContactsRepository.kt`
- `app/src/main/java/org/wastingnotime/contactsmobile/interfaces/ContactsBffBootstrap.kt`
- `app/src/test/java/org/wastingnotime/contactsmobile/infrastructure/config/ContactsBffAuthHeadersResolverTest.kt`
- `app/src/test/java/org/wastingnotime/contactsmobile/infrastructure/http/HttpContactsBffClientTest.kt`
- `app/src/test/java/org/wastingnotime/contactsmobile/infrastructure/http/DefaultContactsRepositoryTest.kt`
- `app/src/test/java/org/wastingnotime/contactsmobile/application/LoadContactsTest.kt`
- `app/src/test/java/org/wastingnotime/contactsmobile/application/CreateContactTest.kt`
- `app/src/test/java/org/wastingnotime/contactsmobile/application/UpdateContactTest.kt`
- `app/src/test/java/org/wastingnotime/contactsmobile/application/DeleteContactTest.kt`
- `./gradlew test`

## Summary

The built contacts-client behavior matches the transport-boundary intent. The app routes list, detail, create, update, and delete requests through the BFF client seam, applies explicit request claims headers to every request, and preserves the existing list/detail UI behavior while broadening the transport contract.

The deterministic tests cover:

- request-claims resolution and header injection
- list/detail/create/update/delete transport mapping
- repository mapping into domain contacts
- use-case behavior for list, create, update, and delete flows

## Findings

### 1. The BFF transport boundary is coherent across the CRUD surface

Observed behavior:

- `HttpContactsBffClient` applies `x-auth-subject` and `x-auth-roles` on every request
- list, detail, create, update, and delete all flow through the same BFF client seam
- the repository maps the BFF transport shape into domain contacts
- the app-level bootstrap composes the BFF client, repository, and use cases in one place

Assessment:

- the transport boundary is explicit and consistent
- the client is not silently relying on anonymous access
- the BFF boundary is the right level of coupling for the current repository state

### 2. The CRUD flows remain aligned with the existing UI expectations

Observed behavior:

- `LoadContacts` returns loaded or empty list states
- `CreateContact` produces a created contact and keeps the flow reachable
- `UpdateContact` reflects updated contact data back into the visible app flow
- `DeleteContact` removes the contact and returns the app to the list view

Assessment:

- the list/detail/create/edit/delete flows remain coherent with the current UI model
- there is no blocking mismatch between the transport boundary and the visible app behavior

### 3. The remaining tension is historical wording, not behavior

Observed behavior:

- some older slice docs still use direct-API wording
- the code now uses `ContactsBffClient`
- the implementation notes have been aligned with the BFF naming

Assessment:

- this is doc drift, not a runtime defect
- the repository still carries some historical slice filenames, but the internal vocabulary is now aligned with the BFF boundary

## Review Questions

1. Do we want to rename the remaining older slice docs to BFF terminology, or leave them as historical slice names?
2. Should the transport boundary be treated as a release candidate now, or do we want one more doc sweep for vocabulary consistency first?

## Recommendation

Continue to release.

Reasoning:

- the implemented behavior matches the transport-boundary intent
- tests pass deterministically
- the remaining gap is limited to historical wording in older slice docs, not behavior
