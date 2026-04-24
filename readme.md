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

The repository currently includes `python_ddd_monolith` as the example selected pack. If the product evolves toward another shape such as Go, TypeScript, event sourcing, or a multi-runtime client/server system, keep the MRL core and replace the pack guidance intentionally.

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
- `architecture.md`
- `groundrules.md`
- `docs/building/project_structure.md`

Before writing substantial project-specific code, decide whether the adopting repository will keep the repository's default split license, where MRL starter and workflow material stays under MIT and all other source code, documentation, and artifacts use MPL 2.0, or intentionally replace it with another license or split-license model. Record that choice clearly in the root `LICENSE` and `decisions.md`.

Then start the loop:

1. run `extract` to build the first semantic baseline in `docs/semantics/`
2. run `refine` to define the first slice in `docs/slices/`
3. run `build` to implement one vertical slice
4. run `egd` to review the built behavior

The semantic placeholders in `docs/semantics/` are intentionally empty. They are meant to be filled by the `extract` phase, not by copying domain content from this starter.
Preserve original evidence in `work/sources/` before extraction or refinement artifacts are produced elsewhere.

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
/src/                      # implementation root shaped by the selected pack
/tests/                    # executable specification
```

---

## Notes

- Treat this repository as a real project with reusable MRL scaffolding, not just as a starter.
- Keep domain specifics out of the starter and in the adopting repository.
- Keep the MRL loop generic and move language or architecture assumptions into packs.
- Make licensing an explicit repository choice; this repository can be reused under one license or adapted into a split-license repository when process material and implementation code need different terms.
- Treat `work/` as repository memory, not scratch space; preserve original evidence in `work/sources/` before downstream artifacts are created.
- Prefer one small slice over broad scaffolding.
