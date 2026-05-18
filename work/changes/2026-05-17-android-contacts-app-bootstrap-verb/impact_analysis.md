# Impact Analysis: Android Mobile App Bootstrap Verb

## Context

The startup path is already explicit through the app-start facade. The remaining pressure is the method name itself: `start()` is generic, while `bootstrap()` would better communicate what the activity is asking for.

## Impacted Areas

- `docs/semantics/model_hypothesis.md`
- `docs/slices/android_contacts_app_bootstrap_verb.md`
- `app/src/main/java/org/wastingnotime/contactsmobile/interfaces/ContactsAppStart.kt`
- `app/src/main/java/org/wastingnotime/contactsmobile/interfaces/MainActivity.kt`
- startup-related tests

## Model Pressure

The pressure here is verb clarity.

The repository should be able to explain app startup with one explicit app-level facade and one explicit bootstrap verb.

## Next Slice Boundaries

The next build slice should rename the app-start method to `bootstrap()`.

That means:

- keep the build-configuration source unchanged
- keep the configuration resolver unchanged
- keep the dependency resolver unchanged
- keep the use-case assembly unchanged
- keep the view-model factory assembly unchanged
- keep the final bootstrap assembly unchanged
- keep the app-start facade object unchanged
- change only the method name used by `MainActivity`

## Constraints

- do not change contacts behavior
- do not introduce new transport logic
- do not widen the slice into UI behavior changes
- do not bypass the existing startup resolution seams
