# Implementation

- Added a shared freshness state to loaded list and loaded detail UI models.
- Marked preserved list and detail data as stale after transient refresh, reload, or delete-related failures.
- Rendered an explicit stale-data banner on the list and detail surfaces.
- Kept the existing retry, search, sort, and CRUD flows unchanged.

Validation:
- `./gradlew test`
- `git diff --check`
