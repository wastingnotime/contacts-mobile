# Impact Analysis: Android Mobile BFF Bootstrap

## Context

The transport contract is already explicit, but `MainActivity` still manually assembles BFF configuration, surface, headers, client, repository, and use cases. The next slice is to make that wiring one readable bootstrap step.

## Impacted Areas

- `docs/semantics/model_hypothesis.md`
- `docs/slices/android_contacts_bff_bootstrap.md`
- `app/src/main/java/org/wastingnotime/contactsmobile/interfaces/MainActivity.kt`
- `app/src/main/java/org/wastingnotime/contactsmobile/infrastructure/http/HttpContactsBffClient.kt`
- `app/build.gradle.kts`
- BFF bootstrap-related tests

## Model Pressure

The pressure here is interface-layer thinness.

The app should be able to explain how it starts without spreading BFF wiring across the activity:

- configuration resolution should happen once
- BFF client assembly should happen once
- the activity should only choose the bootstrap and start the UI

## Next Slice Boundaries

The next build slice should introduce one bootstrap seam for the mobile BFF setup.

That means:

- keep the BFF surface, auth, and base URL behavior unchanged
- centralize client and repository construction
- keep `MainActivity` focused on bootstrapping and rendering
- add deterministic tests for successful and invalid bootstrap assembly

## Constraints

- do not change user-visible contacts behavior
- do not move use-case logic into bootstrap code
- do not reintroduce direct backend coupling
- do not expand the slice into new feature work
