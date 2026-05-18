# Mobile Contract

## Scope

This contract describes the app-facing mobile surface.

It covers:

- the primary contacts experience
- screen and navigation expectations
- list, detail, create, edit, and delete surface behavior
- what a visitor should assume about the repository's mobile entry points

## Stable Semantics

- the repository is a bounded mobile client, not a backend authority
- the app should keep the user-facing flow explicit and inspectable
- visible mobile behavior should remain decoupled from Android implementation details

## Notes

Implementation details such as Compose component structure, ViewModel layout, and local persistence belong elsewhere.
