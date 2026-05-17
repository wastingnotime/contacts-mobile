# Impact Analysis: Android Mobile Bootstrap Rename

## Context

The startup path is already explicit and the app-start facade now has one entry point. The remaining pressure is the final bootstrap object's name: it still carries BFF-specific vocabulary even though it is the object handed to the app activity.

## Impacted Areas

- `docs/semantics/model_hypothesis.md`
- `docs/slices/android_contacts_bootstrap_rename.md`
- `app/src/main/java/org/wastingnotime/contactsmobile/interfaces/ContactsBffBootstrap.kt`
- `app/src/main/java/org/wastingnotime/contactsmobile/interfaces/MainActivity.kt`
- startup-related tests

## Model Pressure

The pressure here is final-object naming.

The repository should be able to explain app startup with one app-start facade and one app-level final bootstrap object.

## Next Slice Boundaries

The next build slice should rename the final bootstrap object away from BFF-specific naming.

That means:

- keep the build-configuration source unchanged
- keep the configuration resolver unchanged
- keep the dependency resolver unchanged
- keep the use-case assembly unchanged
- keep the view-model factory assembly unchanged
- keep the app-start facade unchanged
- rename only the final bootstrap object type handed to the activity

## Constraints

- do not change contacts behavior
- do not introduce new transport logic
- do not widen the slice into UI behavior changes
- do not bypass the existing startup resolution seams
