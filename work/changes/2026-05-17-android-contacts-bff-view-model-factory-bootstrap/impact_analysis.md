# Impact Analysis: Android Mobile BFF View-Model Factory Bootstrap

## Context

The startup path now resolves raw build values, normalizes them, assembles BFF dependencies, assembles use cases, and assembles the `ContactsViewModelFactory` in separate seams. The remaining inline step inside `ContactsBffBootstrapper` is the wrapping of that factory into the final `ContactsBffBootstrap` object.

## Impacted Areas

- `docs/semantics/model_hypothesis.md`
- `docs/slices/android_contacts_bff_view_model_factory_bootstrap.md`
- `app/src/main/java/org/wastingnotime/contactsmobile/interfaces/ContactsBffBootstrap.kt`
- `app/src/main/java/org/wastingnotime/contactsmobile/interfaces/MainActivity.kt`
- startup-related tests

## Model Pressure

The pressure here is final bootstrap locality.

The repository should be able to explain app startup in six explicit steps:

- raw build values are read once
- they are normalized once into bootstrap configuration
- resolved BFF dependencies are assembled once
- application use cases are assembled once
- the view-model factory is assembled once
- the final bootstrap object is wrapped once before being handed to the activity

## Next Slice Boundaries

The next build slice should introduce a dedicated final-bootstrap assembly seam.

That means:

- keep the build-configuration source unchanged
- keep the configuration resolver unchanged
- keep the dependency resolver unchanged
- keep the use-case assembly unchanged
- keep the factory assembly unchanged
- move the `ContactsBffBootstrap` wrapping step out of the bootstrapper body

## Constraints

- do not change contacts behavior
- do not introduce new transport logic
- do not widen the slice into UI behavior changes
- do not bypass the existing startup resolution seams
