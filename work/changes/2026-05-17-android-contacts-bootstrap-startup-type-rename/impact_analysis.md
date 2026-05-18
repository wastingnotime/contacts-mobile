# Impact Analysis: Android Mobile Bootstrap Startup Type Rename

## Context

The app-level bootstrap rename is complete, but the startup configuration and dependency types still carry BFF-specific names. That keeps the startup layer speaking transport vocabulary even where it only models app startup.

## Impacted Areas

- `docs/semantics/model_hypothesis.md`
- `docs/slices/android_contacts_bootstrap_startup_type_rename.md`
- `app/src/main/java/org/wastingnotime/contactsmobile/interfaces/ContactsBffBootstrap.kt`
- `app/src/main/java/org/wastingnotime/contactsmobile/interfaces/ContactsBffBootstrapConfigurationResolver.kt`
- `app/src/main/java/org/wastingnotime/contactsmobile/interfaces/ContactsBffBootstrapBuildConfigurationSource.kt`
- startup-related tests

## Model Pressure

The pressure here is startup-layer naming consistency.

The repository should be able to explain app startup with app-level names for the startup configuration and dependency graph, while keeping BFF names for the actual transport boundary.

## Next Slice Boundaries

The next build slice should rename the startup-layer BFF-branded types to app-level types.

That means:

- keep the transport client, auth headers, API surface, and repository unchanged
- keep the final bootstrap object unchanged
- keep the app-start facade unchanged
- rename only the startup configuration and dependency types and their resolvers/sources

## Constraints

- do not change contacts behavior
- do not rename the transport-layer BFF client or route objects
- do not widen the slice into UI behavior changes
- do not bypass the existing startup resolution seams
