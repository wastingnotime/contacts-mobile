# Impact Analysis: Android Contacts Local Search Visible Fields Only

## Summary

The current search implementation is local and deterministic, but it still matches on the contact id. The next bounded refinement is to narrow search to visible fields only so the behavior aligns with what users see on screen.

## Affected Boundaries

- application layer: local filter matching rules
- interface layer: filtered list results and empty-state behavior
- existing search-summary behavior: should remain intact
- backend contract: should remain unchanged

## Expected Effect

- searches feel more user-facing and less implementation-specific
- contact ids no longer influence result matching
- the visible experience stays deterministic and local

## Risks

- removing id matching could surprise any workflow that was depending on it for debugging or operator-style lookup
- the filter may become too narrow if visible fields are not defined clearly enough
- the existing search-summary and filtered-empty states must continue to reflect the narrowed matching rule

## Validation

- add deterministic tests for visible-field matching and hidden-id exclusion
- confirm the clear-search action and summary still behave correctly
- confirm detail, create, edit, delete, sort, and filtered-empty flows remain reachable
