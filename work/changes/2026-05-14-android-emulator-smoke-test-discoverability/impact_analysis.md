# Impact Analysis: Android Emulator Smoke Test Discoverability

## Summary

The repository already has a full emulator smoke-test runbook and command reference. The remaining gap is discoverability from the top-level docs. This slice should surface that guidance without changing application behavior or the detailed operating procedures.

## Affected Boundaries

- `README.md` root entry docs
- possibly `docs/operating/skills_workflow.md` if a workflow-level pointer is helpful
- existing emulator smoke-test operating docs

## Expected Effect

- Faster operator onboarding for emulator smoke testing
- Less chance of treating `Unable to load contacts` as an app defect when the backend simulation is simply not running
- Clearer separation between repository-owned Android behavior and runtime-sandbox-owned backend simulation

## Risks

- A redundant pointer could drift from the canonical runbook if the wording is copied instead of linked
- Over-linking can make the entry docs noisy, so the pointer should stay short and direct

## Validation

- Documentation-only change
- Confirm root-level navigation makes the runbook discoverable
- Confirm wording still points to `../runtime-sandbox` for backend simulation

