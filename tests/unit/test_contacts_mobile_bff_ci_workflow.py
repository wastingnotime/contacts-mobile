from pathlib import Path


ROOT = Path(__file__).resolve().parents[2]
WORKFLOW = ROOT / ".github/workflows/ci-contacts-mobile-bff.yml"
DOCKERFILE = ROOT / "server/Dockerfile"


def test_contacts_mobile_bff_workflow_publishes_and_dispatches():
    workflow = WORKFLOW.read_text(encoding="utf-8")

    assert "name: ci-contacts-mobile-bff" in workflow
    assert "server/**" in workflow
    assert "go test ./..." in workflow
    assert "Validate publish inputs" in workflow
    assert "Repository secret ECR_ROLE_ARN is required" in workflow
    assert "aws-actions/configure-aws-credentials@v6.1.1" in workflow
    assert "aws-actions/amazon-ecr-login@v2" in workflow
    assert "docker/build-push-action@v7" in workflow
    assert "contacts-mobile-bff" in workflow
    assert "candidate-image-updated" in workflow
    assert '\"service\": \"contacts-mobile-bff\"' in workflow
    assert "wastingnotime/integration-sandbox" in workflow


def test_contacts_mobile_bff_dockerfile_builds_the_server_binary():
    dockerfile = DOCKERFILE.read_text(encoding="utf-8")

    assert "FROM golang:1.25-bookworm AS build" in dockerfile
    assert "COPY cmd ./cmd" in dockerfile
    assert "COPY internal ./internal" in dockerfile
    assert "go build -trimpath -o /out/contacts-bff ./cmd/contacts-bff" in dockerfile
    assert "ENTRYPOINT [\"/contacts-bff\"]" in dockerfile
