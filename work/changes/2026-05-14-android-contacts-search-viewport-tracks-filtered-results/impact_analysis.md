# Impact Analysis: Android Contacts Search Viewport Tracks Filtered Results

## Summary

The app already preserves search context and a general list viewport. The next bounded refinement is to define how those two pieces interact when the query changes the filtered result set.

## Affected Boundaries

- interface layer: list presentation and search interaction
- view-model or presentation layer: viewport state retention across query changes
- existing sort, filtered-empty, stale-data, CRUD, and navigation behavior: should remain unchanged
- backend contract: should remain unchanged

## Expected Effect

- users can refine search without losing their place whenever the current anchor remains visible
- the list feels stable instead of snapping to the top on every query change
- the interaction stays deterministic and local

## Risks

- the fallback rule could feel surprising if the wrong visible row is chosen after filtering changes
- viewport restoration must not fight the filtered-empty state
- search-summary and clear-search actions need to stay consistent with the restored position

## Validation

- add deterministic tests for viewport behavior across query changes
- confirm search-summary, filtered-empty, stale-data, CRUD, and navigation flows remain reachable
- confirm the backend contract remains unchanged
