# Domain Background Knowledge

## Purpose

This document captures broad background knowledge about the target domain.

It is not the repository's extracted glossary. It is reference material distilled from books, articles, standards, external systems, or other domain sources.

Use it mainly during expectation-gap detection.

---

## Current Background

Contacts apps usually imply a simple and inspectable interaction pattern:

- the user expects a list of people with names and phone numbers
- loading should be visible and not silent
- an empty state should be explicit rather than looking like a failure
- network failure should be distinguished from an empty data set
- retry should be available when the transport fails

The backend contract in this repository exposes snake_case JSON fields for contacts:

- `id`
- `first_name`
- `last_name`
- `phone_number`

The mobile app should preserve that transport contract at the boundary and map it into app-facing models internally.

Evaluation should watch for:

- accidental camelCase assumptions at the HTTP boundary
- hiding network failures behind empty screens
- coupling the UI directly to transport payloads
- making the app require live network behavior in unit tests
