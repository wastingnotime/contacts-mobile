# Impact Analysis: Android Mobile App Bootstrap Single Entry

## Context

The startup path is already explicit and renamed to app-level vocabulary. The remaining pressure is surface area: `ContactsAppStart` still exposes an unused config overload even though the activity only needs one bootstrap entry.

## Impacted Areas

- `docs/semantics/model_hypothesis.md`
- `docs/slices/android_contacts_app_bootstrap_single_entry.md`
- `app/src/main/java/org/wastingnotime/contactsmobile/interfaces/ContactsAppStart.kt`
- `app/src/main/java/org/wastingnotime/contactsmobile/interfaces/MainActivity.kt`
- startup-related tests

## Model Pressure

The pressure here is entry-surface minimality.

The repository should be able to explain app startup with one facade, one verb, and one entry point.

## Next Slice Boundaries

The next build slice should remove the unused app-start overload.

That means:

- keep the build-configuration source unchanged
- keep the configuration resolver unchanged
- keep the dependency resolver unchanged
- keep the use-case assembly unchanged
- keep the view-model factory assembly unchanged
- keep the final bootstrap assembly unchanged
- keep the app-start facade object unchanged
- remove the configuration-taking overload from the app-start facade

## Constraints

- do not change contacts behavior
- do not introduce new transport logic
- do not widen the slice into UI behavior changes
- do not bypass the existing startup resolution seams
