# Android Emulator Smoke Test Outcomes Matrix Impact Analysis

## Slice Intent

Keep the runbook procedural and add a compact outcomes matrix that tells the operator what healthy smoke-test progress looks like at each step.

The repository already has the commands and the diagnostic guidance. The remaining pressure is to summarize the expected results in a way that is easy to scan during manual validation.

## Why This Slice Now

Operators need a quick way to tell whether a step is progressing normally or exposing an environment problem. The matrix should answer that without repeating the full smoke-test sequence.

## Impacted Boundaries

### Documentation

- add a compact step-to-outcome matrix
- keep the runbook as the procedural source of truth

### Exposure Workflow

- preserve the backend-first sequence
- preserve the `../runtime-sandbox` dependency
- keep the matrix separate from session recording and failure interpretation

### Android Client

- do not change app code
- do not change smoke-test runtime behavior

## Model Pressure

The pressure is expectation clarity, not behavior.

The matrix should answer:

- what should the operator see when the smoke path is healthy?

It should not restate the whole runbook or the failure matrix.

## Build Guidance

If this slice is built later, the implementation should:

- keep the table short
- focus on observable outcomes at each step
- avoid duplicating the failure interpretation matrix
- avoid creating backend-ownership language in this repository
