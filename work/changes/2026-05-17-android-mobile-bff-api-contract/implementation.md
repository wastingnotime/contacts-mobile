# Implementation: Centralize The BFF Route Surface

## Summary

Moved the fixed BFF `/api/contacts` path contract out of `HttpContactsBffClient` and into a dedicated route object so the transport seam has one explicit place for route assembly.

## What Changed

- Added `ContactsBffRoutes` to centralize the list and item route surface
- Updated `HttpContactsBffClient` to resolve all contacts requests through the route object
- Added tests for the route resolver itself
- Kept the existing BFF base URL, auth headers, and transport mapping behavior intact

## Validation

- `./gradlew test`

The test suite passed after the route abstraction refactor.
