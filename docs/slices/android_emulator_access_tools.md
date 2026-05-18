# Android Emulator Access Tools

- pack: android_compose_client
- runtime_targets:
  - android/app
- architecture_mode: layered_android_client

## Discovery Scope

Document the small set of direct emulator access tools operators can use when they need low-level inspection during smoke testing.

This slice does not change app behavior. It keeps the access-tools guidance short, explicit, and subordinate to the main smoke-test runbook.

The access-tools note should make it explicit that:

- `adb` is the direct emulator inspection tool
- the Android Studio MCP server is the structured IDE-level alternative
- the tools are for diagnosis, logs, screenshots, and explicit activity launches
- the tools do not replace the backend-readiness or session-recording guidance

## Use-Case Contract

No new business use case is introduced.

This slice documents operator tooling for manual validation. It does not change runtime behavior or the smoke-test sequence.

## Main Business Rules

- the access-tools guidance should stay short and practical
- `adb` and the Android Studio MCP server should remain the only named direct-access tools
- the runbook should remain the source of truth for smoke-test sequencing
- the client repo should not imply ownership of emulator operations beyond inspection and capture
- the note should not repeat the readiness, classification, outcomes, or session-record guidance

## Required Ports

None.

This is a documentation slice only.

## Initial Test Plan

- verify the access-tools guidance names `adb` explicitly
- verify the access-tools guidance names the Android Studio MCP server explicitly
- verify the guidance stays separate from the readiness, classification, and record-template slices
- verify the guidance does not duplicate the smoke-test runbook procedure
- verify no runtime app behavior changes are introduced

## Scenario Definition

Given an operator needs to inspect the emulator during smoke testing, the repository should point them to `adb` or the Android Studio MCP server for direct access without turning the runbook into a diagnostics manual.

Given the smoke-test workflow is already documented elsewhere, the access-tools guidance should remain a compact helper rather than a full operating guide.

## Done Criteria

- the emulator access-tools guidance is documented in the repository
- the guidance remains compact and diagnostic
- the runbook still owns the step-by-step smoke-test sequence
- no runtime app behavior changes are introduced
