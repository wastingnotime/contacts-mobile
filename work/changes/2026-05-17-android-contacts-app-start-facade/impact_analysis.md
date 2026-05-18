# Impact Analysis: Android Mobile App Start Facade

## Context

The startup path is already fully explicit through final bootstrap assembly. The remaining pressure is terminology: `MainActivity` still depends on a BFF-specific startup name even though it only needs an app-level startup facade.

## Impacted Areas

- `docs/semantics/model_hypothesis.md`
- `docs/slices/android_contacts_app_start_facade.md`
- `app/src/main/java/org/wastingnotime/contactsmobile/interfaces/MainActivity.kt`
- startup-related tests

## Model Pressure

The pressure here is boundary naming.

The repository should be able to explain app startup using app-level vocabulary while keeping the BFF-specific wiring inside the startup chain.

## Next Slice Boundaries

The next build slice should rename the startup facade to an app-level object.

That means:

- keep the build-configuration source unchanged
- keep the configuration resolver unchanged
- keep the dependency resolver unchanged
- keep the use-case assembly unchanged
- keep the view-model factory assembly unchanged
- keep the final bootstrap assembly unchanged
- change only the startup facade name that `MainActivity` calls

## Constraints

- do not change contacts behavior
- do not introduce new transport logic
- do not widen the slice into UI behavior changes
- do not bypass the existing startup resolution seams
