# Android Contacts Bootstrap Rename

- pack: android_compose_client
- runtime_targets:
  - android/app
  - go/bff
  - external-api/axiom-exp-contacts
- architecture_mode: layered_android_client

## Discovery Scope

Keep the current startup behavior intact while renaming the final bootstrap object to an app-level name:

- preserve the existing contacts flows and startup behavior
- keep raw build-value sourcing, startup normalization, dependency resolution, use-case assembly, factory assembly, app-start facade, and bootstrap verb unchanged
- rename the final bootstrap object away from BFF-specific naming
- keep `MainActivity` consuming the same startup output, just with app-level vocabulary

## Use-Case Contract

No new domain use case is introduced.

This slice changes only the naming of the final bootstrap object for the existing contacts use cases:

- `LoadContacts`
- `LoadContactById`
- `CreateContact`
- `UpdateContact`
- `DeleteContact`

The use cases should still run exactly as before, but the final object handed to the activity should be named for the app boundary rather than the repository-owned BFF boundary.

## Main Business Rules

- the final bootstrap object should be named for the app boundary
- the startup chain should remain explicit and behavior-preserving
- the activity should not need to care about BFF-specific naming for the final bootstrap
- invalid startup configuration should still fail early in the existing resolution path

## Required Ports

- `ContactsAppStart`
- `ContactsBffBootstrapper`
- `ContactsBffBootstrap`
- optional app-final bootstrap rename for the Android entry point

## Initial Test Plan

- verify the final bootstrap type uses app-level naming
- verify `ContactsAppStart.bootstrap()` still returns the final bootstrap object
- verify `MainActivity` continues to consume the final bootstrap object
- verify the build-configuration source, configuration resolver, dependency resolver, use-case assembly, factory assembly, final bootstrap, and app-start tests still pass unchanged

## Scenario Definition

Given valid build values, the app should source raw startup values once, normalize them once, resolve BFF dependencies once, assemble application use cases once, assemble the view-model factory once, wrap the final bootstrap once, hand that bootstrap through one startup facade, invoke `bootstrap()`, and reach the existing UI with no visible behavior change.

If build values are invalid, the startup flow should fail clearly before the app tries to construct a partial client, repository, use-case graph, UI factory, or final bootstrap object.

## Done Criteria

- the app module compiles in the Android Gradle project shape
- the final bootstrap object is named for the app boundary
- deterministic tests cover the bootstrap rename and the existing startup resolution seams
- the repository documents the Android pack and final-bootstrap boundary explicitly
