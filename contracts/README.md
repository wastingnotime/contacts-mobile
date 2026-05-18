# Contracts

This directory is the public boundary for Contacts Mobile.

Use it to understand what the repository exposes to visitors, integrators, and other agents without reading Android implementation details.

## Contract Index

- `mobile/`: app-facing interaction contract, navigation expectations, and surface behavior
- `auth/`: identity, session, and request-claims expectations
- `sync/`: synchronization posture, freshness rules, and conflict handling
- `offline/`: degraded-mode and offline expectations
- `telemetry/`: mobile telemetry semantics and visibility rules
- `notifications/`: notification semantics and delivery expectations

## Boundary Rule

Treat these files as the exported contract layer.

- stable exported semantics belong here
- experimental exported semantics belong here only when they are intentionally part of the public boundary
- internal Android implementation details do not belong here

If a behavior is only meaningful to `app/` internals, keep it out of `contracts/`.
