# Android Contacts App Preview Theme Matrix Impact Analysis

## Slice Intent

Make the Android client easier to inspect in Android Studio by expanding the app-shell preview surface to cover the important visible UI states in both light and dark themes.

This is a design-time refinement, not a product behavior change.

## Why This Slice Now

The app already has a released stale-data preservation behavior and an app-shell preview state matrix. The next useful refinement is to make that shell easier to review under both theme conditions because Compose previews are often used to catch visual regressions in tone, contrast, and spacing.

The pressure is around visibility and iteration speed:

- the app should be easy to inspect without launching a device
- the main state combinations should be visible in design time
- theme variants should be represented so the app shell can be reviewed in the context it will actually ship in
- preview code should stay deterministic and not drag runtime dependencies into the design surface

## Impacted Boundaries

### Interfaces

- the `ContactsApp` preview surface needs theme variants for the representative shell states
- preview helpers may need light and dark wrappers around the existing state matrix

### Application

- no application use case changes are expected

### Infrastructure

- no infrastructure changes are expected

### Tests

- no new business tests are expected
- the build should still validate the project compiles with preview code present

## Model Pressure

The current model already accepts the main contacts list, contact detail, and transient failure states.

This slice pressures the design-time representation of those states under multiple themes, not the behavior itself. It should expose the same model in a more inspectable way.

## Build Guidance

Keep the slice small and UI-only.

It should not:

- alter runtime contacts loading behavior
- alter API contract handling
- introduce navigation or persistence
- make previews depend on live repository or network access
