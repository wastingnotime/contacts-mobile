# Android Emulator Smoke Test Discoverability Impact Analysis

## Slice Intent

Keep the detailed emulator smoke-test guidance in `docs/operating/` while surfacing a short root-level pointer in `README.md`.

The repo already documents the smoke-test procedure. The remaining pressure is discoverability: a reader starting from the root should be able to find the runbook quickly without guessing where the detailed operating docs live.

## Why This Slice Now

The top-level README is the natural entry point for human operators. A short pointer there reduces the chance that backend setup issues get misread as client defects simply because the operator never found the runbook.

This slice stays narrow and linking-only.

## Impacted Boundaries

### Documentation

- add a root-level pointer to the emulator smoke-test runbook
- keep the runbook as the detailed source of truth

### Exposure Workflow

- preserve the explicit `../runtime-sandbox` dependency
- keep the discoverability note short and direct

### Android Client

- do not change app code
- do not change smoke-test runtime behavior

## Model Pressure

The pressure is findability, not behavior.

The repository needs to make one thing obvious from the root:

- where the emulator smoke-test runbook lives

The slice should not duplicate the smoke-test procedure, outcomes matrix, or failure interpretation matrix.

## Build Guidance

If this slice is built later, the implementation should:

- keep the pointer in the root README
- link directly to the runbook
- mention `../runtime-sandbox` explicitly
- avoid expanding into additional workflow prose
