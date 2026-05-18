# Android Emulator Access Tools Impact Analysis

## Slice Intent

Keep the emulator smoke-test runbook concise by separating the direct access tools into their own compact note.

The runbook already covers the repeatable backend-backed smoke-test procedure. The remaining pressure is to keep the low-level emulator tooling easy to find without letting it swallow the procedure itself.

## Why This Slice Now

Operators occasionally need `adb` or the Android Studio MCP server for inspection, log capture, screenshots, or explicit activity launches. That tooling is useful, but it should not become a substitute for the runbook or the backend-readiness guidance.

This slice keeps the tooling note short and helper-like.

## Impacted Boundaries

### Documentation

- add a compact access-tools note
- keep the runbook as the procedural source of truth

### Exposure Workflow

- preserve the backend-first smoke-test sequence
- keep emulator inspection separate from smoke-test sequencing

### Android Client

- do not change app code
- do not change smoke-test runtime behavior

## Model Pressure

The pressure is tooling discoverability, not behavior.

The repository should answer:

- which direct tools are appropriate when the operator needs low-level emulator access?

The note should not repeat the smoke-test runbook, the outcomes matrix, or the session record template.
The note should not imply ownership of backend readiness or session recording in this repository.

## Build Guidance

If this slice is built later, the implementation should:

- keep the note short and practical
- name `adb` and the Android Studio MCP server explicitly
- avoid duplicating the smoke-test procedure
- avoid creating backend-ownership language in this repository
