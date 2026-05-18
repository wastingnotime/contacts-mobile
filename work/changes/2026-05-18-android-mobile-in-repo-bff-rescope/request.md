# Request: Re-scope Contacts Mobile To Own Its Go BFF

## Stakeholder Input

The repository should own its own repository-owned Go BFF rather than treating it as a separate repository boundary.

## Requested Change

Re-scope the repository model so the Android client and the Go BFF are both part of this repository.

## Boundaries

- keep the Android client as the user-facing runtime
- keep the repository-owned Go BFF as the client-facing transport boundary
- keep the downstream `contacts-api` as the backend system behind the BFF
- do not design implementation details in this request artifact

## Expected Artifact Updates

- update the semantic model to reflect in-repo BFF ownership
- update broad domain background knowledge to match the new ownership model
- update top-level architecture and project docs so they stop describing the BFF as external
