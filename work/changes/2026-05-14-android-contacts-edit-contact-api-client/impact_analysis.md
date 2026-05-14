# Impact Analysis: Android Contacts Edit Contact API Client

## Summary

The mobile client can already read and create contacts. The backend API also exposes `PUT /contacts/{id}`, so the next bounded product slice is to add an edit-contact flow that starts from the selected contact detail and keeps the existing flows intact.

## Affected Boundaries

- application layer: new `UpdateContact` use case
- infrastructure layer: HTTP request/response mapping for `PUT /contacts/{id}`
- interface layer: edit form and success/failure state handling
- existing list/detail/create navigation: must remain intact

## Expected Effect

- users can edit an existing contact from the Android app
- the client exercises the backend update contract explicitly
- the app moves closer to a complete contacts-management surface with consistent CRUD behavior

## Risks

- the edit form can drift from the backend's accepted update fields if the prefill contract is underspecified
- updating the list after a successful edit may need a clear rule for preserving the current detail view versus navigating back to it
- the request/response mapping must remain deterministic so tests do not become coupled to live backend behavior

## Validation

- add deterministic use-case tests for update success and update failure
- add transport tests for request/response mapping
- confirm the existing list, detail, create, and stale-data behaviors stay unchanged

