# Sync Contract

## Scope

This contract describes synchronization semantics between the mobile client, the repository-owned BFF, and the backend.

## Stable Semantics

- synchronization is explicit and observable
- refresh behavior should preserve user trust
- last known data may remain visible during transient failure
- sync rules should be documented rather than inferred from code

## Notes

If realtime synchronization is introduced later, it should be written here as a deliberate public contract change.
