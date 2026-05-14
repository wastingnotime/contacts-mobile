# Android Emulator Smoke Test Discoverability

## Goal

Make the emulator smoke-test path easy to find from the repository entry docs so operators do not have to discover the `docs/operating/` runbook by memory.

## Model Pressure

The repo already documents a concrete emulator smoke-test flow, but the guidance is buried behind several operating docs and slice artifacts. That makes manual validation fragile: an operator can launch the app, see `Unable to load contacts`, and miss the adjacent runtime-sandbox-backed commands that explain how to bring up a healthy backend simulation.

This slice does not change app behavior. It tightens the documentation boundary between:

- repository entry points for humans
- detailed smoke-test operating guidance
- the separate `../runtime-sandbox` backend simulation repo

## Scope

- Add a visible pointer to the emulator smoke-test runbook from the repository root docs.
- Keep the runtime-sandbox dependency explicit.
- Keep the existing runbook as the detailed source of commands and outcomes.

## Non-Goals

- Do not change Android runtime code.
- Do not change the backend simulation repo.
- Do not add new smoke-test commands or alter the expected outcomes matrix.

## Acceptance Criteria

- A reader starting from the repository root can find the emulator smoke-test runbook without guessing the file path.
- The pointer makes it clear that backend simulation comes from `../runtime-sandbox`.
- Existing runbook content remains the source of truth for commands, outcomes, and failure interpretation.

## Likely Files

- `README.md`
- optionally a short pointer in `docs/operating/skills_workflow.md` if needed for workflow visibility

