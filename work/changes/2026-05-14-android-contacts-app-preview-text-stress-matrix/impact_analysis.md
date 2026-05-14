# Android Contacts App Preview Text Stress Matrix Impact Analysis

## Slice Intent

Make the Android client easier to inspect in Android Studio by expanding the app-shell preview surface to cover the main visible states with long text values.

This is a design-time refinement, not a product behavior change.

## Why This Slice Now

The app already has released stale-data preservation behavior and app-shell preview coverage for state, theme, width, and font scale. The next useful refinement is to make the shell easier to review when contact text is unusually long, because long names and phone numbers are a common source of layout pressure.

The pressure is around visibility and iteration speed:

- the app should be easy to inspect without launching a device
- the main state combinations should be visible in design time
- text-heavy cases should be represented so the app shell can be reviewed under wrapping, truncation, and header pressure
- preview code should stay deterministic and not drag runtime dependencies into the design surface

## Impacted Boundaries

### Interfaces

- the `ContactsApp` preview surface needs text-stress variants for the shell states
- preview helpers may need long-name and long-number data fixtures around the existing state, theme, width, and font-scale matrices

### Application

- no application use case changes are expected

### Infrastructure

- no infrastructure changes are expected

### Tests

- no new business tests are expected
- the build should still validate the project compiles with preview code present

## Model Pressure

The current model already accepts the main contacts list, contact detail, transient failure states, theme variants, width variants, and font-scale variants.

This slice pressures the design-time representation of those states with longer text values, not the behavior itself. It should expose the same model in a more inspectable way.

## Build Guidance

Keep the slice small and UI-only.

It should not:

- alter runtime contacts loading behavior
- alter API contract handling
- introduce navigation or persistence
- make previews depend on live repository or network access
