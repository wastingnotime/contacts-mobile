# Implementation

- Narrowed the local search use case so it matches only visible contact fields instead of internal identifiers.
- Preserved the existing search-summary, clear-search, filtered-empty, and sorted-list behavior.
- Added a deterministic test that proves id-only queries no longer match.

Validation:
- `./gradlew test`
- `git diff --check`
