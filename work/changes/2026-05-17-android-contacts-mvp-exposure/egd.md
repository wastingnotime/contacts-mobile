# Android Contacts MVP Exposure EGD

## Exposure Record

`work/changes/2026-05-17-android-contacts-mvp-exposure/exposure.md`

## Evidence Reviewed

- `docs/semantics/model_hypothesis.md`
- `docs/semantics/domain_background_knowledge.md`
- `work/changes/2026-05-17-android-contacts-mvp-exposure/exposure.md`
- release and acceptance artifacts for the built slices
- `./gradlew test`

## Summary

The exposure record matches the accepted internal release state. The plan points the released Android contacts MVP at realistic contexts: Android Studio previews, emulator or local-device runtime, and manual smoke testing against the configured repository-owned BFF environment.

The exposure record stays within the expected boundary for this repository. It does not claim backend-sandbox ownership, persistence, or telemetry, and it correctly leaves backend simulation with the adjacent runtime sandbox workflow.

## Findings

### 1. The exposure target matches the released MVP

Observed behavior:

- the exposure plan references the accepted repository-owned BFF-backed list, detail, create, edit, and delete flows
- the plan includes preview review and runtime installation, which are the natural first contact points for this client
- the plan includes the BFF request-claims and backend-sandbox context that the release depends on

Assessment:

- this is aligned with the model hypothesis and the accepted release
- no exposure mismatch is visible at the artifact level

### 2. The exposure boundary is kept in the right place

Observed behavior:

- the exposure plan explicitly avoids taking ownership of backend simulation setup
- the exposure note stays focused on putting the released client into contact with reality
- the plan does not introduce new semantics or requirements

Assessment:

- this matches the repository's boundary discipline
- the exposure step remains a lifecycle handoff, not a new product slice

### 3. No blocking expectation gap remains

Observed behavior:

- the accepted release artifacts and tests are already green
- the exposure plan is consistent with the client's actual responsibilities

Assessment:

- the exposure handoff is semantically coherent
- no new model refinement is required from this review

## Review Questions

1. Should the first real exposure be preview review, emulator smoke test, or a local-device install?
2. Do we want to record exposure feedback in a future `living` artifact once the MVP is exercised in a real context?

## Recommendation

Continue to expose.

Reasoning:

- the release is already accepted
- the exposure plan matches the release boundary
- there is no blocking expectation gap between the release and the planned real-world contact points
