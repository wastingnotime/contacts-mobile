# Impact Analysis: Android Mobile BFF Bootstrap Dependencies

## Context

The startup path now has separate seams for raw build-value sourcing and startup configuration normalization, but the resolved BFF client and repository are still assembled inline inside `ContactsBffBootstrapper`. That keeps the dependency graph local to the bootstrapper rather than in a single resolved dependency object.

## Impacted Areas

- `docs/semantics/model_hypothesis.md`
- `docs/slices/android_contacts_bff_bootstrap_dependencies.md`
- `app/src/main/java/org/wastingnotime/contactsmobile/interfaces/ContactsBffBootstrap.kt`
- `app/src/main/java/org/wastingnotime/contactsmobile/interfaces/MainActivity.kt`
- startup-related tests

## Model Pressure

The pressure here is dependency-graph locality.

The repository should be able to explain startup in three explicit steps:

- raw build values are read once
- they are normalized once into bootstrap configuration
- the resolved BFF client and repository are assembled once into a dependency object

## Next Slice Boundaries

The next build slice should introduce a dedicated BFF dependency object for startup assembly.

That means:

- keep the build-configuration source unchanged
- keep the configuration resolver unchanged
- move resolved client/repository assembly out of the bootstrapper body
- keep the final UI bootstrap shape unchanged

## Constraints

- do not change contacts behavior
- do not introduce new transport logic
- do not widen the slice into use-case or UI changes
- do not bypass the existing startup resolution seams
