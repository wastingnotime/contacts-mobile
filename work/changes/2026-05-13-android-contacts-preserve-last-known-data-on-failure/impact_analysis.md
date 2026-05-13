# Android Contacts Preserve Last Known Data On Failure Impact Analysis

## Slice Intent

Refine the current Android contacts client so that transient reload failures do not erase already loaded content.

The app already has list and detail flows, plus explicit retry. The next semantic pressure is whether a refresh failure should replace content with a blank error screen or keep the last successful data visible.

## Why This Slice Now

The current implementation remains honest about failure, but it is visually harsh: once a refresh fails, the app replaces the view with an error state. For a contacts app, that can be too destructive when the user already has useful data on screen.

This slice narrows the behavior to:

- preserve the last successful list on list refresh failure
- preserve the last successful detail on detail reload failure
- keep failure explicit and retryable

## Impacted Boundaries

### Interfaces

- the UI state model needs to distinguish "content + failure" from "no content yet + failure"
- the list screen and detail screen should be able to render stale content with an error affordance

### Application

- refresh and reload paths need to keep the previous successful result available when a reload fails
- the existing read use cases can stay in place

### Infrastructure

- no new network contract is required
- existing claims headers and transport mapping remain unchanged

### Tests

- add deterministic tests for stale-content preservation on list and detail failures
- add tests ensuring not-found remains distinct from transient failure

## Model Pressure

The current model hypothesis now treats transient failure retention as the remaining unresolved tension. This slice resolves that pressure in favor of preserving last known data while keeping the failure visible.

## Build Guidance

The build should make the smallest state-model change needed to preserve stale successful content on reload failure.

It should not:

- add disk persistence yet
- introduce a navigation library
- change auth or transport behavior
- collapse not-found and transient failure into the same state

