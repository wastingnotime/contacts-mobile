# Impact Analysis: Android Mobile BFF Bootstrap Build Configuration Source

## Context

`ContactsBffBootstrapper.build()` now delegates raw startup-value normalization to `ContactsBffBootstrapConfigurationResolver`, but it still names every `BuildConfig` field directly. That keeps raw app build values embedded in the bootstrapper instead of in one dedicated source seam.

## Impacted Areas

- `docs/semantics/model_hypothesis.md`
- `docs/slices/android_contacts_bff_bootstrap_build_configuration_source.md`
- `app/src/main/java/org/wastingnotime/contactsmobile/interfaces/ContactsBffBootstrap.kt`
- `app/src/main/java/org/wastingnotime/contactsmobile/interfaces/MainActivity.kt`
- startup-related tests

## Model Pressure

The pressure here is startup-source locality.

The repository should be able to explain app startup in two explicit steps:

- raw build values are read once from one source object
- those values are normalized once into bootstrap configuration

## Next Slice Boundaries

The next build slice should introduce a dedicated build-configuration source for BFF startup.

That means:

- keep the BFF bootstrap assembly unchanged
- keep the existing normalization resolver unchanged
- move raw `BuildConfig` field reads out of the bootstrapper
- keep `MainActivity` focused on orchestration and UI startup

## Constraints

- do not change contacts behavior
- do not introduce new transport logic
- do not add new startup validation rules beyond the existing normalization seam
- do not widen the slice beyond raw build-value sourcing
