# Impact Analysis: Android Mobile BFF Bootstrap Configuration Resolution

## Context

`ContactsBffBootstrapper` still resolves `BuildConfig` values directly. That works, but it leaves the startup configuration mapping embedded in the bootstrapper rather than in one dedicated resolution seam.

## Impacted Areas

- `docs/semantics/model_hypothesis.md`
- `docs/slices/android_contacts_bff_bootstrap_configuration_resolution.md`
- `app/src/main/java/org/wastingnotime/contactsmobile/interfaces/ContactsBffBootstrap.kt`
- `app/src/main/java/org/wastingnotime/contactsmobile/interfaces/MainActivity.kt`
- startup-related tests

## Model Pressure

The pressure here is configuration locality.

The repository should be able to explain startup configuration in one step:

- `BuildConfig` values are read once
- they are mapped into a bootstrap configuration value object once
- the bootstrapper consumes that value object

## Next Slice Boundaries

The next build slice should introduce a dedicated startup-configuration resolver.

That means:

- keep the BFF bootstrap assembly unchanged
- move raw build-value mapping out of the bootstrapper
- keep `MainActivity` focused on orchestration
- add deterministic tests for valid and invalid bootstrap configuration resolution

## Constraints

- do not change any contacts behavior
- do not move application or domain logic into the resolver
- do not add new transport behavior
- do not widen the slice beyond startup configuration mapping
