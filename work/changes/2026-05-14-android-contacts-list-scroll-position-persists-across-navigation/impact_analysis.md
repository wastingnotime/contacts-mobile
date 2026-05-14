# Impact Analysis: Android Contacts List Scroll Position Persists Across Navigation

## Summary

The list search context already survives navigation. The next bounded refinement is to preserve the list viewport so the user can return to the same place in the contacts list after opening detail or forms.

## Affected Boundaries

- interface layer: list presentation and navigation transitions
- view-model or presentation layer: viewport state retention
- existing sort, filter, search-summary, stale-data, and CRUD behavior: should remain unchanged
- backend contract: should remain unchanged

## Expected Effect

- users can scroll once and continue from the same place after brief navigation
- the list feels stable instead of snapping back to the top
- the interaction stays deterministic and local

## Risks

- viewport state could be lost if it is owned by the wrong layer
- list content changes after create, edit, delete, or refresh may need a clear rule for whether the viewport is restored or intentionally reset
- search/filter transitions must remain consistent with the preserved viewport

## Validation

- add deterministic tests for viewport persistence across detail and form navigation
- confirm search, sort, filtered-empty, stale-data, and CRUD flows remain reachable
- confirm the backend contract remains unchanged
