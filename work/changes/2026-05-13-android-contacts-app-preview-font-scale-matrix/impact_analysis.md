# Android Contacts App Preview Font Scale Matrix Impact Analysis

## Slice Intent

Make the Android client easier to inspect in Android Studio by expanding the app-shell preview surface to cover the important visible UI states at representative font scales.

This is a design-time refinement, not a product behavior change.

## Why This Slice Now

The app already has released stale-data preservation behavior, an app-shell state matrix, a theme matrix, and a width matrix. The next useful refinement is to make that shell easier to review under typography scaling because text growth is a common accessibility and layout pressure point.

The pressure is around visibility and iteration speed:

- the app should be easy to inspect without launching a device
- the main state combinations should be visible in design time
- font-scale variants should be represented so the app shell can be reviewed under ordinary and accessibility-style text growth
- preview code should stay deterministic and not drag runtime dependencies into the design surface

## Impacted Boundaries

### Interfaces

- the `ContactsApp` preview surface needs font-scale variants for the shell states
- preview helpers may need density wrappers around the existing state, theme, and width matrix

### Application

- no application use case changes are expected

### Infrastructure

- no infrastructure changes are expected

### Tests

- no new business tests are expected
- the build should still validate the project compiles with preview code present

## Model Pressure

The current model already accepts the main contacts list, contact detail, transient failure states, theme variants, and width variants.

This slice pressures the design-time representation of those states under multiple font scales, not the behavior itself. It should expose the same model in a more inspectable way.

## Build Guidance

Keep the slice small and UI-only.

It should not:

- alter runtime contacts loading behavior
- alter API contract handling
- introduce navigation or persistence
- make previews depend on live repository or network access
