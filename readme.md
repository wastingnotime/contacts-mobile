# Contacts Mobile

Contacts Mobile is a native Android client for the `axiom-exp-contacts` system. The app starts with the contacts list flow and is organized to keep UI, use cases, and transport concerns explicit.

The app should reach `contacts-api` through a Go BFF, not directly. The BFF can begin as a near 1:1 proxy for the backend contract and later evolve without forcing the mobile client to track backend changes.

This repository uses MRL as the working method for shaping the codebase. For workflow details, read:

- `docs/operating/mrl_reference.md`
- `docs/operating/skills_workflow.md`

## Project Shape

The repository currently follows the `android_compose_client` pack and uses Jetpack Compose for the interface layer.

The client should talk to a Go BFF, which in turn talks to `contacts-api`. The first executable slice is the contacts list screen, which loads `GET /contacts` through that BFF and renders loading, empty, error, and list states.

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

## Working Notes

- Treat `work/` as repository memory, not scratch space.
- Preserve original evidence in `work/sources/` before creating downstream artifacts.
- Keep the MRL loop generic and move implementation specifics into the selected pack and slice documents.
- When a change is completed, commit it before starting the next one.
