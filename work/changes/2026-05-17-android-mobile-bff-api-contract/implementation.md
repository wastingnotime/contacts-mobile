# Implementation: Normalize Contacts Requests Onto The BFF `/api` Surface

## Summary

Updated the Android client transport seam so all contacts requests resolve through the fixed Go BFF `/api` prefix instead of assembling backend paths directly.

## What Changed

- Added the `/api` prefix to every contacts request in `HttpContactsBffClient`
- Kept the existing BFF base URL and request claims behavior intact
- Updated the BFF client tests to assert the actual request paths for list, detail, create, update, and delete flows

## Validation

- `./gradlew test`

The test suite passed after the path contract normalization.
