# Android Emulator Smoke Test Runbook Impact Analysis

## Slice Intent

Keep the emulator smoke-test runbook focused on the repeatable procedure and move the adjacent diagnostic and recording concerns into their own slices.

The repository already has a command reference, readiness preflight, session classification, outcomes matrix, failure interpretation matrix, and session record template. The remaining pressure is to make the runbook concise again so it serves as the procedural backbone rather than a catch-all document.

## Why This Slice Now

The runbook has started to absorb responsibilities that belong to nearby docs. That makes it harder to scan during validation.

The better refinement is to keep the runbook centered on:

- the sandbox-backed backend source
- the sequence for preparing the backend and installing the app
- the brief app-validation flow

## Impacted Boundaries

### Documentation

- trim the runbook to the repeatable procedure
- keep the adjacent docs as the source of truth for classification, outcomes, failure interpretation, and session recording

### Exposure Workflow

- preserve the explicit `../runtime-sandbox` dependency
- keep backend setup separate from manual app validation

### Android Client

- do not change app code
- do not change smoke-test runtime behavior

## Model Pressure

The pressure is document separation, not behavior.

The runbook should answer:

- what is the repeatable sequence for validating the Android client on an emulator?

It should not carry the detailed classification template, failure matrix, or outcomes matrix.

## Build Guidance

If this slice is built later, the implementation should:

- keep the steps short and sequential
- avoid repeating the session record template
- avoid repeating the classification labels
- avoid repeating the full failure/outcomes matrices
