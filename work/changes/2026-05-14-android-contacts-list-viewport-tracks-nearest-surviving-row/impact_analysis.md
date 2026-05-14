# Impact Analysis: Android Contacts List Viewport Tracks Nearest Surviving Row

## Summary

The list viewport already survives navigation and list mutations when the anchor contact exists. The next bounded refinement is to make the fallback behavior explicit when that anchor disappears.

## Affected Boundaries

- interface layer: list presentation and CRUD transitions
- view-model or presentation layer: viewport state retention across content changes
- existing sort, filter, search-summary, stale-data, CRUD, and navigation behavior: should remain unchanged
- backend contract: should remain unchanged

## Expected Effect

- users stay near the same list neighborhood even when the anchor row disappears
- the list feels stable after delete or refresh changes that remove the current anchor
- the interaction stays deterministic and local

## Risks

- the fallback rule could feel surprising if the wrong neighbor is chosen
- viewport state could still be reset if the anchor logic is owned by the wrong layer
- search/filter transitions must remain consistent with the preserved viewport

## Validation

- add deterministic tests for nearest-row fallback when the anchor disappears
- confirm search, sort, filtered-empty, stale-data, CRUD, and navigation flows remain reachable
- confirm the backend contract remains unchanged
