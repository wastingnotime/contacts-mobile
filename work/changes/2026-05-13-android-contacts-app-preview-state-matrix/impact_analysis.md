# Android Contacts App Preview State Matrix Impact Analysis

## Slice Intent

Make the Android client easier to inspect in Android Studio by expanding the app-shell preview surface to cover the important visible UI states.

This is a design-time refinement, not a product behavior change.

## Why This Slice Now

The app already has a released stale-data preservation behavior and a basic `ContactsApp` preview. The next useful refinement is to make the app shell easier to review as it evolves, especially when main-screen states change over time.

The pressure is around visibility and iteration speed:

- the app should be easy to inspect without launching a device
- the main state combinations should be visible in design time
- preview code should stay deterministic and not drag runtime dependencies into the design surface

## Impacted Boundaries

### Interfaces

- the `ContactsApp` composable or an adjacent preview wrapper needs representative state inputs
- preview helpers may need to supply loading, empty, list, detail, and transient-failure variants

### Application

- no application use case changes are expected

### Infrastructure

- no infrastructure changes are expected

### Tests

- no new business tests are expected
- the build should still validate the project compiles with preview code present

## Model Pressure

The current model already accepts the main contacts list, contact detail, and transient failure states.

This slice pressures the design-time representation of those states, not the behavior itself. It should expose the same model in a more inspectable way.

## Build Guidance

Keep the slice small and UI-only.

It should not:

- alter runtime contacts loading behavior
- alter API contract handling
- introduce navigation or persistence
- make previews depend on live repository or network access
