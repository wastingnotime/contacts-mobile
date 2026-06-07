# syntax=docker/dockerfile:1.7

FROM golang:1.26-bookworm AS build

WORKDIR /src

ARG TARGETOS
ARG TARGETARCH

COPY go.mod go.sum ./
COPY cmd ./cmd
COPY internal ./internal

RUN CGO_ENABLED=0 GOOS="$TARGETOS" GOARCH="$TARGETARCH" go build -trimpath -o /out/contacts-bff ./cmd/contacts-bff

FROM gcr.io/distroless/static-debian12:nonroot

COPY --from=build /out/contacts-bff /contacts-bff
COPY --from=build /etc/ssl/certs/ca-certificates.crt /etc/ssl/certs/ca-certificates.crt

EXPOSE 8080

HEALTHCHECK --interval=30s --timeout=3s --start-period=5s --retries=3 CMD ["/contacts-bff", "healthcheck"]

USER nonroot:nonroot

ENTRYPOINT ["/contacts-bff"]
