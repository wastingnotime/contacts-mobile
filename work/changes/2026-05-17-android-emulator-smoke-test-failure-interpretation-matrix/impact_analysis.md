# Android Emulator Smoke Test Failure Interpretation Matrix Impact Analysis

## Slice Intent

Keep the emulator smoke-test runbook intact while adding a compact matrix that maps observed failures to likely causes.

The repository already has the detailed procedure and the expected outcomes. The remaining pressure is to make failure interpretation fast and diagnostic so operators can separate environment issues from client regressions at a glance.

## Why This Slice Now

Manual smoke tests still need help when something goes wrong. The operator should not have to infer from the runbook alone whether a symptom means:

- the backend sandbox is not ready
- the Android client install or launch path is broken
- the emulator cannot reach the configured base URL
- the observed state is actually expected

This slice keeps the matrix short and diagnostic.

## Impacted Boundaries

### Documentation

- add a failure-signature-to-meaning matrix
- keep the runbook as the procedural source of truth

### Exposure Workflow

- preserve backend-first ordering
- preserve `../runtime-sandbox` as the backend source
- keep the diagnostic matrix separate from the session record template

### Android Client

- do not change app code
- do not change smoke-test runtime behavior

## Model Pressure

The pressure is interpretation clarity, not behavior.

The matrix should answer one question quickly:

- what does this failure probably mean?

It should not repeat the smoke-test steps, the readiness preflight, or the session record template.

## Build Guidance

If this slice is built later, the implementation should:

- keep the matrix compact
- focus on likely meaning rather than step-by-step recovery
- avoid duplicating the full runbook
- avoid creating backend-ownership language in this repository
