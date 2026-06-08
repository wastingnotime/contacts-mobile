# Finding: contacts-mobile-bff missing health endpoints in image healthcheck

## Issue Type

- finding

## Context

Where this was discovered:
- integration validation against the published `contacts-mobile-bff` image

## Observed Behavior

The published image healthcheck only probed `/health/ready`, so downstream validation could not confirm the full runtime health surface from the container metadata alone.

The repository runtime already exposed both endpoints, but the Docker `HEALTHCHECK` contract did not reference both of them.

## Expected Behavior

The published candidate image should expose a Docker `HEALTHCHECK` that references both `/health/live` and `/health/ready`.

## Impact

Downstream image validation could not treat the published `contacts-mobile-bff` candidate as contract-compliant until the healthcheck metadata matched the runtime contract.

## Suspected Source

`wastingnotime/contacts-mobile`

## Evidence

- GitHub Actions validation failure on `2026-06-07T19:59:23Z`
- Error text: `does not expose required health endpoints: /health/live, /health/ready`
- Issue: [wastingnotime/contacts-mobile#29](https://github.com/wastingnotime/contacts-mobile/issues/29)

## Suggested Direction

Update the container healthcheck command so the image metadata explicitly probes both health endpoints.

## Owning Repository

`wastingnotime/contacts-mobile`

## Local Impact

- keep the repository-owned runtime contract and published image healthcheck aligned
- close the finding once the image healthcheck and issue resolution are recorded

## Resolution

Resolved in commit `c8602f5` and tracked in GitHub issue [#29](https://github.com/wastingnotime/contacts-mobile/issues/29), which has been closed.
