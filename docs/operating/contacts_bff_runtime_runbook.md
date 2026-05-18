# Contacts BFF Runtime Runbook

## Purpose

This runbook gives the minimal local command for starting the repository-owned Go BFF.

Keep this file short. It is a runnable note, not a deployment manual.

---

## Start Command

From the repository root:

```bash
cd server
CONTACTS_API_BASE_URL=http://127.0.0.1:8081 go run ./cmd/contacts-bff
```

The server listens on `:8080` unless `CONTACTS_BFF_LISTEN_ADDR` is set.

---

## Required Inputs

- `CONTACTS_API_BASE_URL`: base URL for the downstream `contacts-api`

---

## Optional Inputs

- `CONTACTS_BFF_LISTEN_ADDR`: overrides the local listen address
- `CONTACTS_BFF_API_PREFIX`: overrides the `/api` surface prefix
- `CONTACTS_BFF_AUTH_SUBJECT`: request-claims subject header value
- `CONTACTS_BFF_AUTH_ROLES`: request-claims roles header value

---

## Smoke Check

When the BFF is running, `GET /api/contacts` should return JSON and the request-claims headers should remain visible at the transport edge.
