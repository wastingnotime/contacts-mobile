# Contacts Mobile

## Purpose

This repository contains the Contacts Mobile project.

It is shaped with MRL so AI-assisted work stays explicit, testable, and tied to repository artifacts rather than conversational memory.

MRL is the method used here, not the product. The product concern is the Contacts Mobile codebase and the behavior it implements.

Use the MRL loop to refine this repository deliberately:

```text
extract -> refine -> build -> egd -> release -> expose -> living -> extract
```

This repository separates:

- MRL core: the artifact-driven refinement loop and workflow
- implementation packs: selectable language and architecture defaults

The repository currently includes `android_compose_client` as the selected pack. The product is a native Android client that consumes the external `axiom-exp-contacts` API and starts with the contacts list flow.

One of the repository's main characteristics is that pre-defined packs can be used to scaffold implementation choices while the MRL workflow keeps the modeling, slicing, build, and evaluation phases explicit.

---

## Getting Started

Clone this repository into the new repository name you want:

```bash
git clone <starter-url> my-new-project
cd my-new-project
```

The preferred way to start a new project is to use this repository as a GitHub template, not as a fork. Forking is more appropriate when working on the repository itself or maintaining a derivative starter.

Then read these files first:

- `docs/operating/mrl_reference.md`
- `docs/operating/skills_workflow.md`
- `docs/operating/packs.md`
- `docs/operating/best_practices.md`
- `docs/operating/emulator_smoke_test_runbook.md` for emulator validation with backend simulation from `../runtime-sandbox`
- `architecture.md`
- `groundrules.md`
- `docs/building/project_structure.md`

This repository keeps the default split-license model recorded in `LICENSE` and `decisions.md`: MRL starter and workflow material stays under MIT, while all other source code, documentation, and artifacts use MPL 2.0.

Then start the loop:

1. run `extract` to build the first semantic baseline in `docs/semantics/`
2. run `refine` to define the first slice in `docs/slices/`
3. run `build` to implement one vertical slice
4. run `egd` to review the built behavior

The semantic placeholders in `docs/semantics/` are intentionally empty. They are meant to be filled by the `extract` phase, not by copying domain content from this starter.
Preserve original evidence in `work/sources/` before extraction or refinement artifacts are produced elsewhere.

For this repository, the first executable slice is an Android contacts list screen that loads `GET /contacts` from the backend and renders loading, empty, error, and list states.

For repositories that expect to use `expose`, released artifacts should normally be packaged in a portable runtime form, with a container image as the default. That packaging rule belongs to MRL operating guidance rather than to any specific adopting repository's domain semantics.

---

## Starter Layout

```text
.agents/skills/            # repo-local MRL skills
/docs/operating/           # MRL model and workflow docs
/docs/packs/               # implementation pack definitions
/docs/building/            # structure and bootstrap guidance
/docs/evaluation/          # expectation-gap evaluation guidance
/docs/semantics/           # domain-specific meaning created by extract
/docs/slices/              # one slice document per increment
/work/sources/             # canonical folder for curated raw evidence and original source material
/work/changes/             # request, impact, and implementation artifacts
/app/                      # Android application module shaped by the selected pack
```

---

## Notes

- Treat this repository as a real project with reusable MRL scaffolding, not just as a starter.
- Keep domain specifics out of the starter and in the adopting repository.
- Keep the MRL loop generic and move language or architecture assumptions into packs.
- Make licensing an explicit repository choice; this repository can be reused under one license or adapted into a split-license repository when process material and implementation code need different terms.
- Treat `work/` as repository memory, not scratch space; preserve original evidence in `work/sources/` before downstream artifacts are created.
- Prefer one small slice over broad scaffolding.
