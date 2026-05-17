# Decisions Log

## Purpose

This document records relevant architectural and implementation decisions for the adopting repository.

Use it to preserve reasoning, avoid re-discussing settled trade-offs without context, and document deviations from `architecture.md` and `groundrules.md`.

---

## Entry Template

```md
## DEC-XXXX - Title

- Date: YYYY-MM-DD
- Status: proposed | accepted | superseded | rejected
- Owners: human | codex | both

### Context
What problem or tension led to this decision?

### Decision
What was decided?

### Consequences
What becomes easier, harder, or different because of this?

### Alternatives considered
What other options were considered and why were they not chosen?

### Notes
Any additional implementation guidance, migration note, or follow-up.
```

---

## Index

Add entries as the repository evolves.

## DEC-0001 - Separate MRL Core From Implementation Packs

- Date: 2026-03-29
- Status: superseded
- Owners: both

### Context
The starter was presenting Python plus a DDD-inspired modular monolith as if that were the default shape of MRL itself. That creates confusion when a repository needs another language, another architecture such as event sourcing, or more than one runtime.

### Decision
The repository now distinguishes between:

- MRL core, which stays artifact-driven and architecture-agnostic
- implementation packs, which define language, architecture, structure, and testing defaults

The current repository keeps `python_ddd_monolith` as the example selected pack.

### Consequences
It becomes easier to reuse the same refinement workflow across Python, JavaScript, Go, event-sourced, and polyglot client/server repositories. It also becomes necessary to make the selected pack explicit in architecture docs and slice docs.

### Alternatives considered
Keep one universal Python starter and treat every other shape as an undocumented deviation. This was rejected because it would keep conflating MRL with one implementation style.

### Notes
Future pack additions should live under `docs/packs/` and should be referenced by slice documents when the runtime topology matters.
Superseded by `DEC-0006` for the repository's selected implementation pack.

## DEC-0002 - Treat Skill Model Guidance As Advisory

- Date: 2026-04-02
- Status: accepted
- Owners: both

### Context
The repository skills now include model guidance for tasks such as `build`, `refine`, and `extract`. That creates a potential ambiguity: a reader could assume that naming a preferred model in a skill will automatically switch the active Codex model or force sub-agent routing during execution.

### Decision
Model guidance inside repository skills is advisory only. It documents which model shape is usually a good fit for the task, but it does not by itself require automatic model switching, worker spawning, or hard routing behavior.

### Consequences
The skills remain durable even if model names, availability, or routing capabilities change. The repository gains clearer guidance for future operators and tooling, but predictable model selection still requires explicit runtime policy or orchestration outside the skill text.

### Alternatives considered
Encode specific model names in skills as if they were enforced execution rules. This was rejected because skill text alone does not guarantee runtime behavior and would overstate what the repository can currently control.

### Notes
If the repository later wants deterministic skill-to-model routing, document that as a separate operational decision and implement it in the calling workflow or agent orchestration layer.

## DEC-0003 - Avoid `.codex` Repository Artifacts For Now

- Date: 2026-04-02
- Status: accepted
- Owners: both

### Context
The repository has used `.codex`-specific artifacts while shaping the workflow. That creates a risk that the MRL process starts to look tool-defined rather than process-defined before it is clear whether MRL can stay tool-agnostic in practice.

### Decision
For now, the repository should avoid relying on `.codex` as part of the committed process shape. The workflow should stay centered on MRL artifacts and repository documents rather than tool-specific folders. This decision is explicitly reversible while the team validates whether MRL can remain tool-agnostic or whether a specific AI tool will prove operationally necessary.

### Consequences
The repository stays cleaner and more focused on portable process artifacts. It may require some extra translation when using Codex or another tool because fewer tool-native conventions are captured directly in versioned files. The decision also preserves room to adopt tool-specific support later if real usage shows that generic artifacts are not enough.

### Alternatives considered
Keep `.codex` as a first-class part of the repository workflow immediately. This was rejected for now because it would prematurely optimize for one tool before validating whether that coupling is necessary for MRL.

### Notes
If future evidence shows that MRL depends on stable capabilities from a specific AI tool, record a follow-up decision describing the required coupling, why generic artifacts were insufficient, and which tool-specific assets should become part of the repository.

## DEC-0004 - Commit After Each Completed Change

- Date: 2026-04-24
- Status: accepted
- Owners: both

### Context
The workflow needs a stable memory rule that keeps completed work visible in version control instead of leaving multiple finished changes bundled together in an uncommitted working tree.

### Decision
After every completed change, create a commit before starting the next change. The commit should stay scoped to the completed slice or documentation update so the repository history mirrors the MRL artifact trail.

### Consequences
Completed work becomes easier to inspect, revert, and reference. The history also stays aligned with the repository's explicit-artifact model because each finished change has a corresponding versioned checkpoint.

### Alternatives considered
Batch several completed changes into a later commit. This was rejected because it weakens traceability and makes the artifact trail less precise.

### Notes
This rule applies to documentation-only changes as well as code changes.

## DEC-0005 - Split MRL Starter Material From MPL 2.0 Repository Content

- Date: 2026-04-24
- Status: accepted
- Owners: both

### Context
The repository needs a durable rule that distinguishes the MRL starter and workflow material from the repository content that future adopters will treat as project-specific.

### Decision
Keep the MRL starter and workflow material under MIT. License all other source code, documentation, and artifacts under MPL 2.0.

### Consequences
The starter can remain reusable as process material while project-specific code and docs can use a file-level copyleft license that fits the adoption model. Contributors and downstream maintainers get a clearer boundary between reusable MRL infrastructure and repository-specific content.

### Alternatives considered
Use a single license for the entire repository. That was rejected because it would collapse the distinction between reusable MRL process material and project-specific content.

### Notes
The root `LICENSE` should keep this split visible, and repository-specific source files should add license headers or notices when the MPL 2.0 scope needs to be explicit per file.

## DEC-0006 - Adopt A Native Android Compose Client Pack

- Date: 2026-05-13
- Status: accepted
- Owners: both

### Context
The requested product for this repository is no longer a backend/lab monolith. The adjacent repositories now provide the contacts API and the web frontend, and this repository needs to become a native Android client that consumes the backend directly.

### Decision
The repository now adopts an `android_compose_client` pack. The implementation should use Kotlin, the Android Gradle project model, and Jetpack Compose for the interface layer. The first slice is an API-backed contacts list screen that loads `GET /contacts` and renders loading, empty, error, and list states.

### Consequences
The starter Python pack is no longer the right mental model for implementation. The repo structure should shift toward an Android `app/` module, Kotlin source sets, and explicit transport parsing for the contacts API. The semantic and slice documents should describe client behavior instead of server-side business rules.

### Alternatives considered
Keep the `python_ddd_monolith` example pack and treat the mobile app as a conceptual overlay. That was rejected because it would hide the real runtime and make the repository misleading for future implementation work.

### Notes
The API contract being consumed is the external `axiom-exp-contacts` service. The mobile app should treat its `/contacts` endpoint as the source of truth and keep the base URL configurable for emulator, local, and production environments.

## DEC-0007 - Resolve Contacts API Base URL At Build Time

- Date: 2026-05-13
- Status: accepted
- Owners: both

### Context
The Android client needs to talk to the contacts API in three common environments: emulator, local-device development, and production. Hardcoding a single host makes the app awkward to run outside the emulator and hides the deployment contract.

### Decision
The app now resolves the contacts API base URL from BuildConfig values populated by Gradle properties. The default build targets the emulator host `http://10.0.2.2:8010`. Local-device development uses a separate configurable base URL, and production builds require an explicit production base URL to be provided at configuration time.

### Consequences
The runtime stays simple because the app still receives a single resolved base URL. Build configuration becomes the explicit place where environment selection lives. Production misconfiguration fails fast during Gradle configuration instead of surfacing later as an app launch failure.

### Alternatives considered
Product flavors were considered, but they would multiply variants and complicate the already-working unit-test flow for this small slice. A runtime settings screen was also considered, but that would make environment selection a user-facing concern instead of a build concern.

### Notes
The local-device default uses the host-style loopback URL convention already used elsewhere in the repository. Production must set `contactsApiProductionBaseUrl` explicitly.

## DEC-0008 - Target Android 15 For Play Policy Compatibility

- Date: 2026-05-13
- Status: superseded
- Owners: both

### Context
Google Play's current target API policy requires new apps and updates to target Android 15, and this repository already has the Android 15 SDK installed locally.

### Decision
The Android app module now compiles and targets API level 35. The `minSdk` remains 24 because that is a separate runtime-compatibility choice and already covers Android 15 devices.

### Consequences
The app stays compatible with Android 15 devices and is aligned with the current Play target policy. Existing runtime support is unchanged because `minSdk` did not need to move.

### Alternatives considered
Leaving `targetSdk` at 34 would preserve the current behavior, but it would lag the current Play policy for new submissions and updates. Raising `minSdk` was not necessary and would have reduced device coverage for no benefit.

### Notes
This is a policy compatibility update, not a feature change.

## DEC-0010 - Target Android 16 For The Mobile Client

- Date: 2026-05-14
- Status: superseded
- Owners: both

## DEC-0012 - Build A Debug APK In GitHub Actions

- Date: 2026-05-17
- Status: accepted
- Owners: both

### Context
The repository needs a repeatable CI path that produces an APK a phone can install without requiring a full release-signing setup. The app already builds locally, but there was no pipeline artifact for sharing or sideloading.

### Decision
Add a GitHub Actions workflow that runs the Android unit tests, assembles the debug APK, and uploads the resulting APK as a workflow artifact. Keep the pipeline on the debug signing key for now so it works without extra secrets.

### Consequences
Anyone with access to the workflow run can download an installable APK artifact. The pipeline does not yet produce a Play-ready release APK, and it does not solve signing-key management.

### Alternatives considered
Build a signed release APK in CI. That was not chosen because the repository does not yet have a managed release keystore or secret-handling policy for distribution.

### Notes
If release distribution becomes necessary, add a separate signing flow and keep this debug artifact pipeline as the fast installable path.
The workflow also opts into Node.js 24 for JavaScript-based actions so it stays clear of the current deprecation warning while `android-actions/setup-android` remains on `v3`.

### Context
Android 16 is the desired platform target for the mobile client, and the current Android Gradle plugin line already supports API level 36.

### Decision
The app module now compiles and targets API level 36. The Gradle wrapper stays on 8.13, the Android Gradle plugin stays on 8.13.2, the Java toolchain remains 17, and the Kotlin/Compose stack stays on Kotlin 1.9.24 with Compose compiler 1.5.14 because that combination is already compatible.

### Consequences
The client can adopt Android 16 behavior changes without forcing a Kotlin migration or a Compose compiler restructuring. Build tooling stays on the supported 17/8.13/8.13.2 line.

### Alternatives considered
Upgrading Kotlin to 2.x and moving to the Compose Compiler Gradle plugin was considered, but it is not required for the Android 16 target and would introduce avoidable migration work for the current slice.

### Notes
If future work needs Kotlin 2.x features, revisit the Compose compiler setup and re-evaluate the library stack separately from the platform target update.

## DEC-0011 - Move The Validation Client Onto The AGP 9.2.1 Toolchain

- Date: 2026-05-14
- Status: accepted
- Owners: both

### Context
This repository is serving as the validation phase before the more complex real project starts. The next project should inherit a known-good AGP 9.x baseline rather than repeating the migration under production pressure.

### Decision
The Android client now uses AGP 9.2.1, Gradle 9.4.1, AGP built-in Kotlin support, Kotlin 2.3.21 for the Compose compiler plugin, and the Kotlin Compose compiler plugin itself. The previous AGP 8.13.2 decision is superseded.

### Consequences
The repo absorbs the toolchain transition while the surface area is still small. Future Android work can start from a verified 9.x baseline instead of paying the first upgrade cost later in a larger codebase.

### Alternatives considered
Staying on AGP 8.13.2 would reduce short-term churn, but it would also defer the migration knowledge and leave the next project to absorb the same compatibility work later.

### Notes
This is a toolchain-validation decision, not a product behavior change. The Android 16 target stays in place, and the old `kotlin-android` plugin path is intentionally removed.

## DEC-0009 - Use The `org.wastingnotime` Android Namespace

- Date: 2026-05-13
- Status: accepted
- Owners: both

### Context
The app package had been using `com.wastingnotime.contactsmobile`, but the project identity should follow the `wastingnotime.org` domain convention instead of the `.com` variant.

### Decision
The Android app module now uses `org.wastingnotime.contactsmobile` for its Gradle namespace, application ID, Kotlin packages, and manifest activity reference.

### Consequences
The codebase matches the org-owned domain naming convention consistently. Android Studio, BuildConfig, and generated resources now align with the new namespace.

### Alternatives considered
Keep the existing `com.wastingnotime.contactsmobile` package to avoid a repo-wide rename. That was rejected because it preserved the wrong domain identity.

### Notes
This is a naming and identity correction only. The runtime behavior and API contract are unchanged.
