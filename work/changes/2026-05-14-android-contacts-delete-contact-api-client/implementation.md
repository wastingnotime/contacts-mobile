# Implementation

- Added an API-backed delete-contact flow that calls `DELETE /contacts/{id}` through the repository and HTTP client.
- Extended the contact detail UI with a deleting state and a Delete action from the loaded detail view.
- Kept the list and detail flows consistent with the existing refresh-preservation behavior.
- Added deterministic unit tests for the delete use case, HTTP client request handling, and view-model success/failure behavior.

Validation:
- `./gradlew test`
- `git diff --check`
