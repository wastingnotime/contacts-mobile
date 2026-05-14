# Impact Analysis: Android Contacts Stale Data Indicator On Transient Failure

## Summary

The app already preserves last known data during transient failures. The next bounded refinement is to make that preserved state explicit so users can tell when they are seeing stale information rather than fresh data, including the delete-failure path that leaves preserved detail on screen.

## Affected Boundaries

- interface layer: list and detail banners/badges for stale content
- view-model or presentation layer: freshness state attached to preserved data
- existing retry behavior: should remain unchanged
- backend contract: should remain unchanged

## Expected Effect

- users can see that the content is preserved and stale after a refresh failure
- list and detail surfaces remain honest about freshness without hiding the data
- retry remains available from the stale state
- a delete failure that preserves the current detail should show the same stale freshness treatment

## Risks

- the stale indicator could become visually noisy if it is too prominent
- stale state may be confused with error state if the copy is not precise
- list and detail freshness states need to stay in sync with the preserved-data behavior

## Validation

- add deterministic tests for stale indicators on list and detail surfaces
- add deterministic tests for stale indicators on delete-related preserved detail
- confirm initial load failures still render as hard errors
- confirm retry, search, sort, and CRUD flows remain reachable
