# Release Notes

- Change: `2026-06-07-contacts-mobile-bff-healthcheck-dual-endpoint`
- Date: 2026-06-07
- Repository: `wastingnotime/contacts-mobile`
- Released version: `c8602f5`
- Artifact digest: `n/a`
- Status: accepted

## Summary

The repository-owned Go BFF image healthcheck now verifies both `/health/live` and `/health/ready`, so the published container metadata matches the runtime health contract and downstream validation can confirm both surfaces.

## Features

- Docker `HEALTHCHECK` now invokes `contacts-bff healthcheck /health/live /health/ready`

## Fixes

- Restored parity between the runtime health contract and the published image healthcheck metadata
- Closed GitHub issue [#29](https://github.com/wastingnotime/contacts-mobile/issues/29)

## Infrastructure

- Updated the release image healthcheck contract in `server/Dockerfile`

## Contracts And Compatibility

- Breaking changes: no
- Migrations: n/a
- API changes: runtime health contract unchanged; image metadata now covers both endpoints
- Event changes: n/a

## Observability

- Health surfaces remain explicit in `contracts/runtime/README.md`

## Validation

- Integration summary: n/a
- Test evidence: `go test ./...` in `server/` and `pytest tests/unit/test_contacts_mobile_bff_ci_workflow.py`

## Related

- Campaign: n/a
- Resolved blockers: n/a
- Findings addressed: `wastingnotime/contacts-mobile#29`

## Production Trace

- Infrastructure PR: n/a
- Deployment manifest: n/a
- Production image digest: pending

Do not mark this release as production-deployed unless the production manifest state confirms the digest.
