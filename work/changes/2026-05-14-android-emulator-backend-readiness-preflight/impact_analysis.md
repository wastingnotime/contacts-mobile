# Impact Analysis: Android Emulator Backend Readiness Preflight

## Summary

The emulator smoke-test workflow already exists. The next bounded refinement is to make backend readiness a required preflight so manual smoke-test results are attributed correctly.

## Affected Boundaries

- exposure workflow: emulator/manual smoke-test ordering
- documentation: runbook and exposure guidance
- runtime sandbox boundary: should remain external to this repository
- Android client behavior: should remain unchanged

## Expected Effect

- smoke tests are easier to interpret
- backend setup problems are separated from Android client behavior
- navigation and viewport observations become meaningful only after backend readiness is confirmed

## Risks

- the guidance could be skipped if it is not visible in the runbook
- the repository could drift into owning sandbox operations if the boundary is not explicit

## Validation

- update the runbook or exposure guidance with an explicit readiness preflight
- ensure the guidance clearly distinguishes setup failures from client regressions
- keep the Android app behavior unchanged
