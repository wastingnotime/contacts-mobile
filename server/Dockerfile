# syntax=docker/dockerfile:1.7

FROM golang:1.25-bookworm AS build

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

USER nonroot:nonroot

ENTRYPOINT ["/contacts-bff"]
