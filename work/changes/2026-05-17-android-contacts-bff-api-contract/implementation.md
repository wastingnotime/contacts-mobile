# Implementation: Centralize The BFF API Surface

## Summary

Moved the fixed BFF `/api/contacts` path contract out of `HttpContactsBffClient` and into a dedicated API surface object so the transport seam has one explicit place for route assembly and prefix selection.

## What Changed

- Added `ContactsBffApiSurface` and `ContactsBffApiSurfaceResolver` to centralize the list and item route surface
- Added `contactsBffApiPrefix` build configuration so the surface can be selected explicitly
- Updated `HttpContactsBffClient` to resolve all contacts requests through the API surface object
- Added tests for the API surface resolver itself
- Kept the existing BFF base URL, auth headers, and transport mapping behavior intact

## Validation

- `./gradlew test`

The test suite passed after the route abstraction refactor.
