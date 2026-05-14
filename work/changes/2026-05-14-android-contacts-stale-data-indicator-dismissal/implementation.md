# Implementation

- Added explicit acknowledgement state for stale list and detail surfaces.
- Allowed users to dismiss the stale warning without hiding preserved content.
- Reset the stale acknowledgement when a fresh reload occurs or a new transient failure appears.
- Added deterministic tests for stale banner dismissal and reappearance.

Validation:
- `./gradlew test`
- `git diff --check`
