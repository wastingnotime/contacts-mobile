# Impact Analysis: Android Mobile BFF API Contract

## Context

The mobile client already routes through a Go BFF, but the path contract is still implicit in the client implementation. The next slice makes the `/api` surface explicit so the app does not assemble backend routes ad hoc.

## Impacted Areas

- `docs/semantics/model_hypothesis.md`
- `docs/slices/android_contacts_bff_api_contract.md`
- `app/src/main/java/org/wastingnotime/contactsmobile/infrastructure/http/HttpContactsBffClient.kt`
- `app/src/main/java/org/wastingnotime/contactsmobile/interfaces/MainActivity.kt`
- `app/build.gradle.kts`

## Model Pressure

The pressure here is contract normalization, not new behavior.

The repository should be able to explain the mobile-to-BFF path in one place:

- the BFF base URL decides where the service lives
- the `/api` prefix decides how the client reaches it
- the existing contacts use cases should not each reinvent that path

## Next Slice Boundaries

The next build slice should update the BFF client seam so the mobile app resolves all contacts requests through a fixed `/api/contacts` surface.

That means:

- keep the current use cases and behavior
- centralize the BFF path prefix in the transport layer
- keep request claims and snake_case mapping intact
- add deterministic tests for the normalized list and detail routes, then extend the same boundary to write flows if needed

## Constraints

- do not change the user-visible contacts behavior
- do not reintroduce direct Android-to-backend coupling
- do not hide the `/api` contract behind per-call-site string concatenation
