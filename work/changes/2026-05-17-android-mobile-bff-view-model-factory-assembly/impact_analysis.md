# Impact Analysis: Android Mobile BFF View-Model Factory Assembly

## Context

The startup path now resolves raw build values, normalizes them, assembles BFF dependencies, and assembles use cases in separate seams. The last inline step inside `ContactsBffBootstrapper` is the construction of `ContactsViewModelFactory`.

## Impacted Areas

- `docs/semantics/model_hypothesis.md`
- `docs/slices/android_contacts_bff_view_model_factory_assembly.md`
- `app/src/main/java/org/wastingnotime/contactsmobile/interfaces/ContactsBffBootstrap.kt`
- `app/src/main/java/org/wastingnotime/contactsmobile/interfaces/MainActivity.kt`
- startup-related tests

## Model Pressure

The pressure here is final bootstrap locality.

The repository should be able to explain app startup in five explicit steps:

- raw build values are read once
- they are normalized once into bootstrap configuration
- resolved BFF dependencies are assembled once
- application use cases are assembled once
- the view-model factory is assembled once before the final UI bootstrap

## Next Slice Boundaries

The next build slice should introduce a dedicated view-model factory assembly seam.

That means:

- keep the build-configuration source unchanged
- keep the configuration resolver unchanged
- keep the dependency resolver unchanged
- keep the use-case assembly unchanged
- move the `ContactsViewModelFactory` construction out of the bootstrapper body

## Constraints

- do not change contacts behavior
- do not introduce new transport logic
- do not widen the slice into UI behavior changes
- do not bypass the existing startup resolution seams
