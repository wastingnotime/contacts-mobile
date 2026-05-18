# Impact Analysis: Android Mobile BFF App Start Facade

## Context

The startup path now resolves raw build values, normalizes them, assembles BFF dependencies, assembles use cases, assembles the `ContactsViewModelFactory`, and wraps the final `ContactsBffBootstrap` in separate seams. The remaining direct knowledge in `MainActivity` is the `ContactsBffBootstrapper` type name.

## Impacted Areas

- `docs/semantics/model_hypothesis.md`
- `docs/slices/android_contacts_bff_app_start_facade.md`
- `app/src/main/java/org/wastingnotime/contactsmobile/interfaces/MainActivity.kt`
- startup-related tests

## Model Pressure

The pressure here is entry-point locality.

The repository should be able to explain app startup in seven explicit steps:

- raw build values are read once
- they are normalized once into bootstrap configuration
- resolved BFF dependencies are assembled once
- application use cases are assembled once
- the view-model factory is assembled once
- the final bootstrap object is wrapped once
- the activity calls one named app-start facade

## Next Slice Boundaries

The next build slice should introduce a dedicated app-start facade.

That means:

- keep the build-configuration source unchanged
- keep the configuration resolver unchanged
- keep the dependency resolver unchanged
- keep the use-case assembly unchanged
- keep the view-model factory assembly unchanged
- keep the final bootstrap assembly unchanged
- move the bootstrapper call behind a single startup facade that `MainActivity` uses

## Constraints

- do not change contacts behavior
- do not introduce new transport logic
- do not widen the slice into UI behavior changes
- do not bypass the existing startup resolution seams
