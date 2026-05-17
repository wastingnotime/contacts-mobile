# Impact Analysis: Android Mobile BFF Use-Case Assembly

## Context

The startup path now resolves raw build values, normalizes them, and builds the resolved BFF client and repository in separate seams. The remaining inline step inside `ContactsBffBootstrapper` is the construction of the app use cases and the `ContactsViewModelFactory`.

## Impacted Areas

- `docs/semantics/model_hypothesis.md`
- `docs/slices/android_contacts_bff_use_case_assembly.md`
- `app/src/main/java/org/wastingnotime/contactsmobile/interfaces/ContactsBffBootstrap.kt`
- `app/src/main/java/org/wastingnotime/contactsmobile/interfaces/MainActivity.kt`
- startup-related tests

## Model Pressure

The pressure here is use-case locality.

The repository should be able to explain app startup in four explicit steps:

- raw build values are read once
- they are normalized once into bootstrap configuration
- resolved BFF dependencies are assembled once
- application use cases are assembled once before the final UI bootstrap

## Next Slice Boundaries

The next build slice should introduce a dedicated use-case assembly seam.

That means:

- keep the build-configuration source unchanged
- keep the configuration resolver unchanged
- keep the dependency resolver unchanged
- move the five contacts use-case instantiations out of the bootstrapper body
- keep the final view-model factory shape unchanged

## Constraints

- do not change contacts behavior
- do not introduce new transport logic
- do not widen the slice into UI changes
- do not bypass the existing startup resolution seams
