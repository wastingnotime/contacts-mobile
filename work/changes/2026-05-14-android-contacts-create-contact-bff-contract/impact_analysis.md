# Impact Analysis: Android Contacts Create Contact BFF Contract

## Summary

The mobile client is currently read-only. The BFF already exposes `POST /api/contacts`, so the next meaningful product slice is to add a create-contact write flow that preserves the existing list/detail experience.

## Affected Boundaries

- application layer: new `CreateContact` use case
- infrastructure layer: HTTP request/response mapping for `POST /api/contacts`
- interface layer: create form and success/failure state handling
- existing list/detail navigation: must remain intact

## Expected Effect

- users can create a new contact from the Android app
- the client exercises the backend write contract explicitly
- the app becomes closer to a complete contacts-management surface instead of only a read shell

## Risks

- create validation could drift from backend-required fields if the form contract is underspecified
- success-path navigation may need a small UX decision: return to detail versus return to list with confirmation
- this slice may expose additional transport assumptions if the request/response mapping is not kept deterministic

## Validation

- add deterministic use-case tests for create success and create failure
- add transport tests for request/response mapping
- confirm the existing read flows and stale-data behavior stay unchanged
