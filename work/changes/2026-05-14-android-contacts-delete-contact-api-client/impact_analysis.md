# Impact Analysis: Android Contacts Delete Contact API Client

## Summary

The mobile client already supports reading, creating, and editing contacts. The BFF also exposes `DELETE /api/contacts/{id}`, so the next bounded product slice is to add a delete-contact flow that starts from the selected contact detail and preserves the existing flows.

## Affected Boundaries

- application layer: new `DeleteContact` use case
- infrastructure layer: HTTP request mapping for `DELETE /api/contacts/{id}`
- interface layer: delete action and failure/success state handling
- existing list/detail/create/edit navigation: must remain intact

## Expected Effect

- users can delete an existing contact from the Android app
- the client exercises the backend delete contract explicitly
- the app reaches a more complete CRUD surface with a clear exit from detail after delete

## Risks

- delete confirmation behavior may need clarification if the app should protect against accidental deletes
- preserving the selected contact on delete failure needs a clear retry rule
- the request/response mapping must stay deterministic so tests remain independent of live backend behavior

## Validation

- add deterministic use-case tests for delete success and delete failure
- add transport tests for request mapping
- confirm the existing list, detail, create, edit, and stale-data behaviors stay unchanged
