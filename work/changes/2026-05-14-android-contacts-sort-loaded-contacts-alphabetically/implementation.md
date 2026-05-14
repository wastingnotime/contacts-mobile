# Implementation

- Added a local `SortContacts` use case that orders loaded contacts by visible display name with deterministic id tie-breaking.
- Applied the sorting rule inside the contacts view-model so refresh, create, edit, and delete all maintain the same local order.
- Kept local search operating over the sorted contacts list and preserved the existing detail and form flows.
- Added deterministic tests for alphabetical ordering, tie-breaking, and sorted list rendering.

Validation:
- `./gradlew test`
- `git diff --check`
