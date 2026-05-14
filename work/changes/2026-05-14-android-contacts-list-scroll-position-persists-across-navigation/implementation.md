# Implementation: Android Contacts List Scroll Position Persists Across Navigation

## What Changed

- added `ContactsListViewportState` as a presentation-layer value object
- stored the list viewport in `ContactsViewModel`
- fed the viewport state into `ContactsScreen`
- bound `LazyColumn` to a remembered `LazyListState`
- propagated scroll updates back to the view-model
- added deterministic unit tests for viewport retention across detail and form navigation

## Validation

- `./gradlew test`
- `git diff --check`

## Result

The contacts list now keeps its visible position when the user opens detail or form surfaces and returns to the list.
