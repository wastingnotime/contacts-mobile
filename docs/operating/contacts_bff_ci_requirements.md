# Contacts BFF CI Requirements

## Purpose

This note records the repository-level GitHub Actions inputs used by the Contacts Mobile BFF publish workflow.

Keep this file short. It documents repository settings, not workflow internals.

---

## Workflow

- `.github/workflows/ci-contacts-mobile-bff.yml`

---

## Repository Variables

- `AWS_DEFAULT_REGION`: `us-east-1`
- `ECR_ROLE_NAME`: `github-actions-ecr`

---

## Repository Secrets

- `ECR_ROLE_ARN`: AWS role ARN used for ECR access
- `GH_INTEGRATION_SANDBOX_DISPATCH_TOKEN`: token used to dispatch `candidate-image-updated` to `wastingnotime/integration-sandbox`

---

## Notes

The workflow builds the `contacts-mobile-bff` image, pushes it to ECR, and dispatches the resulting candidate image to integration-sandbox for validation.
The publish job uses the canonical `github-actions-ecr` role ARN from the infrastructure contract in `infra-platform` and still requires `AWS_DEFAULT_REGION` to be configured.
