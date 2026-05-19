# Contacts Mobile

Contacts Mobile is a native Android client plus repository-owned Go BFF for the `axiom-exp-contacts` system. The app starts with the contacts list flow and is organized to keep UI, use cases, transport concerns, and BFF ownership explicit.

The repository should reach `contacts-api` through its Go BFF, not directly. The BFF can begin as a near 1:1 proxy for the backend contract and later evolve without forcing the mobile client to track backend changes.

This repository uses MRL as the working method for shaping the codebase. For workflow details, read:

- `docs/operating/mrl_reference.md`
- `docs/operating/skills_workflow.md`

## Project Shape

The repository currently follows the `android_compose_client` pack and uses Jetpack Compose for the interface layer. It also owns the Go BFF runtime that fronts `contacts-api`.

The client should talk to the repository-owned Go BFF, which in turn talks to `contacts-api`. The first executable slice is the contacts list screen, which loads `GET /contacts` through that BFF and renders loading, empty, error, and list states.

## Public Contracts

The repository exports its mobile-facing boundary through `contracts/`.

- `contracts/mobile/`
- `contracts/auth/`
- `contracts/sync/`
- `contracts/offline/`
- `contracts/telemetry/`
- `contracts/notifications/`

Treat those folders as the public navigation layer for the mobile boundary. Android implementation details stay inside `app/` and do not belong in the exported contract docs.

## Getting Started

Read these files first:

- `architecture.md`
- `groundrules.md`
- `docs/building/project_structure.md`
- `docs/operating/packs.md`
- `docs/operating/best_practices.md`

## Emulator Validation

Use `docs/operating/emulator_smoke_test_runbook.md` for emulator validation.

Backend simulation for that flow comes from `../runtime-sandbox`.

## BFF Runtime

Use `docs/operating/contacts_bff_runtime_runbook.md` for the local Go BFF startup command and required environment.
Use `.github/workflows/ci-contacts-mobile-bff.yml` for the CI path that builds, pushes, and dispatches the candidate BFF image.
Use `docs/operating/contacts_bff_ci_requirements.md` for the repository-level GitHub Actions variables and secrets that support that workflow.

## Campaign Notifications

Campaign issues use GitHub Actions in:

- [`.github/workflows/notify-discord-on-issue-open.yml`](/home/henrique/repos/github/wastingnotime/contacts-mobile/.github/workflows/notify-discord-on-issue-open.yml)
- [`.github/workflows/notify-discord-on-issue-close.yml`](/home/henrique/repos/github/wastingnotime/contacts-mobile/.github/workflows/notify-discord-on-issue-close.yml)

## Working Notes

- Treat `work/` as repository memory, not scratch space.
- Preserve original evidence in `work/sources/` before creating downstream artifacts.
- Keep the MRL loop generic and move implementation specifics into the selected pack and slice documents.
- Use `contracts/` to describe the exported mobile boundary, not the internal Android implementation.
- When a change is completed, commit it before starting the next one.
