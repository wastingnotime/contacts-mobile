# Pack: android_compose_client

## Purpose

Use this pack when the target product is a native Android client that consumes an external HTTP API.

This pack defines reusable implementation defaults for Android repositories that need a thin UI shell, explicit use cases, and a stable API transport boundary.

---

## Shape

- language: Kotlin
- runtime topology: Android client plus external API
- architecture mode: layered Android client with explicit application and infrastructure boundaries

---

## Good Fit

- native Android apps that consume an existing REST API
- client-first products where the UI is part of the validation surface
- slices that need deterministic state, request mapping, and offline-friendly test seams

---

## Less Suitable

- backend services
- pure library modules with no Android runtime
- systems that need a full server/runtime pair inside one repository

---

## Layout

```text
app/
  src/main/java/com/wastingnotime/contactsmobile/
    domain/
    application/
    infrastructure/
    interfaces/

  src/main/res/

  src/test/java/com/wastingnotime/contactsmobile/
    application/
    infrastructure/
    interfaces/

scripts/
```

---

## Boundaries

- business rules live in `domain/` and `application/`
- orchestration lives in `application/`
- persistence and HTTP concerns live in `infrastructure/`
- interface and rendering concerns live in `interfaces/`
- shared contracts with the backend live in slice documents and transport mappers

---

## Testing Defaults

- unit/spec focus: contact mapping, load use cases, state transitions
- integration focus: HTTP transport parsing and repository wiring
- determinism rules: fixed JSON payloads, fake repositories, controlled load outcomes, no live network in unit tests
- preferred test command: `./gradlew test`

---

## Scenario Runner Shape

- preferred runner location: `scripts/` or a dedicated sample activity entry point
- preferred command: `./gradlew :app:test`
- runner responsibility: exercise the contacts list flow against deterministic fixtures
- runner must not: depend on the live network for correctness checks

---

## Runtime Targeting Rules

- slices using this pack should declare runtime targets as:
  - `android/app`
  - `external-api/axiom-exp-contacts`
- cross-runtime contracts should live in slice documents and transport DTOs
- event/message boundaries should be expressed in application use cases and documentation

---

## Naming Conventions

- use cases or commands: verb-driven, such as `LoadContacts`
- domain objects: intention-revealing, such as `Contact`
- repositories/gateways: port names ending in `Repository` or `Client`
- events: past-tense facts if used

---

## Rules

- keep the Android UI thin
- keep API parsing separate from rendering
- make the base URL configurable
- favor deterministic test fixtures over live backend assumptions

---

## Starter Tooling

- package manager: Gradle
- formatter/linter: Kotlin formatting via IDE or ktlint if later added
- test framework: JUnit
- persistence default: in-memory data for tests, HTTP for the live client

---

## Notes

This pack is a reusable default, not a law. If the repository later needs more than an Android client and one external API, record that change in `decisions.md` and refine the pack accordingly.
