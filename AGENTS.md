# Repository Guidelines

## Repository Identity
Contacts Mobile is a bounded mobile repository for the `axiom-exp-contacts` product. It owns the Android client experience and the repository-owned Go BFF boundary that fronts `contacts-api`.

This repository is not a backend authority, a deployment repository, or a general infrastructure workspace. It is a mobile runtime boundary with explicit contracts.

## Repository Responsibilities
This repository owns:

- the Android mobile experience
- app-facing interaction contracts
- auth and session expectations at the mobile boundary
- synchronization semantics between the client, the repository-owned BFF, and the backend
- mobile telemetry and notification expectations
- runtime and device assumptions that affect the client boundary

## Non-Responsibilities
This repository does not own:

- backend business authority
- infrastructure topology outside the mobile boundary
- deployment orchestration
- Android implementation details as public API
- shared domain ownership for unrelated repositories

## Mobile Runtime Philosophy
Treat the repository as a bounded mobile actor. Public contracts and explicit semantics matter more than leaking Android implementation details.

Prefer:

- explicit contracts over hidden coupling
- deterministic local behavior over realism for its own sake
- stable runtime assumptions over accidental environment inference
- thin UI layers over framework-heavy logic

## Visitor Guidance
Start with these files when orienting yourself:

- `readme.md`
- `architecture.md`
- `contracts/README.md`
- `docs/semantics/model_hypothesis.md`
- `docs/semantics/domain_background_knowledge.md`
- `docs/slices/README.md`

Use `docs/` for repository knowledge and `contracts/` for exported boundary meaning. Treat `app/` internals as implementation detail unless you are actively changing the Android client.

## Public Contracts
The repository exports its mobile-facing boundary through `contracts/`.

- `contracts/mobile/`: app-facing interaction contract, surfaces, and navigation expectations
- `contracts/auth/`: session, identity, and request-claims expectations
- `contracts/sync/`: synchronization contract, freshness rules, and conflict posture
- `contracts/offline/`: offline and degraded-mode expectations
- `contracts/telemetry/`: mobile telemetry semantics and visibility rules
- `contracts/notifications/`: notification expectations and user-facing delivery rules

The contract docs describe the external-facing meaning of the repository. They are not a place for Android implementation notes.

## Synchronization Expectations
Synchronization is an explicit contract, not an implied side effect.

- the client may preserve last known data during transient failure
- refresh behavior should be honest about freshness and failure
- realtime behavior is optional unless a contract file says otherwise
- sync semantics should stay visible at the boundary instead of being hidden in screen code

## Offline Semantics
Offline behavior is best-effort by default.

- the app may continue to render last known data when live synchronization fails
- offline behavior should be documented in `contracts/offline/`
- experimental offline capabilities should not be treated as stable unless the contract says so

## Stable vs Experimental Areas
Stable exported semantics:

- auth and session expectations
- synchronization semantics
- telemetry semantics
- notification expectations
- runtime and device assumptions

Experimental areas:

- advanced offline behavior
- realtime synchronization
- AI-assisted mobile flows

## Internal-Only Areas
These are implementation details and should not be treated as public contract surface:

- Android Compose and ViewModel structure
- local persistence implementation
- Gradle and build configuration internals
- repository wiring details inside `app/`
- transport adapters and helper classes that only exist to support the mobile implementation

## Build, Test, and Development Commands
Keep tooling lightweight until the first slice exists.

- `pip install -e .` installs the editable package.
- `pytest` runs the test suite.
- `python -m src.app.interfaces.cli.run_scenario` is the preferred shape for a local scenario runner when one exists.

## Coding Style & Naming Conventions
Prefer Python 3.12+, explicit types, 4-space indentation, and business-oriented names. Use verb-driven use cases such as `PlaceOrder` and intention-revealing repositories such as `get_by_id` and `save`.

## Testing Guidelines
Use tests as specification. Start with domain tests, add integration tests for mappings and end-to-end flows, and keep time, IDs, and external responses deterministic.

## Campaign / Findings Interaction
If a requested change affects more than one repository, look for the current campaign in `/home/henrique/repos/bitbucket/solareclipseglasses/management/campaigns/<campaign-name>/` and follow `/home/henrique/repos/bitbucket/solareclipseglasses/management/references/decisions/campaign_management_protocol_decision.md`.

For cross-repository findings discovered locally, follow `docs/operating/cross_repo_findings_guidance.md`. Create findings in this repository, escalate multi-repository impact to management, and do not implement fixes owned by another repository.
For MRL-managed work resolved by code or configuration changes, follow `docs/operating/issue_pr_linking_guidance.md`. Open a pull request in this repository and link it back to the issue, campaign record, or finding record.

## Ownership Expectations
Changes to public contracts should be reflected in the matching `contracts/` document and, when the change has architectural weight, in `decisions.md`.

Treat campaign handoffs as the coordination layer only. Keep durable repository-specific knowledge, validation, and guidance inside this repository.

## Commit & Pull Request Guidelines
Use Conventional Commits for commit subjects, choosing an appropriate type such as `feat`, `fix`, `docs`, `refactor`, `test`, `build`, `ci`, or `chore`. Keep commits scoped to one slice or doc change, and include test evidence in pull requests.

<!-- mrl-cli agent hint start: wnt-overlay-hints -->
## WNT Overlay Hints

For managed-repository handoffs, follow `docs/operating/extensions/wnt/cross_repo_findings_guidance.md`.

For issue and pull request linking in MRL-managed work, follow `docs/operating/extensions/wnt/issue_pr_linking_guidance.md`.

For delivery or production-status questions, read `docs/operating/extensions/wnt/release_delivery_validation.md`.

This hint block is installed by `mrl-cli` from `mrl-extension-wnt`. It is not repository identity; root strategic docs and the rest of `AGENTS.md` remain owned by this repository.
<!-- mrl-cli agent hint end: wnt-overlay-hints -->