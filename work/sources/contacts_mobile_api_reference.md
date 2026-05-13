# Contacts Mobile API Reference

This note captures the local evidence used to shape the first Android client slice.

## Source Material

- `../axiom-exp-contacts/internal/application/dto.go`
- `../axiom-exp-contacts/internal/httpapi/server.go`
- `../axiom-exp-contacts/internal/httpapi/server_test.go`
- `../contacts-web/src/client/contracts/contactTransport.js`
- `../contacts-web/src/client/api/httpContactsApiClient.js`
- `../contacts-web/tests/contracts/httpContactsApiClient.test.js`

## Extracted Facts

- the backend exposes `GET /contacts`
- contact transport uses snake_case fields:
  - `id`
  - `first_name`
  - `last_name`
  - `phone_number`
- the backend defaults to admin claims when no auth headers are supplied
- the web frontend maps the same transport payload into camelCase UI models
- the mobile app can consume the backend directly without the web BFF

## Implications For The Android Client

- keep the transport mapping at the infrastructure boundary
- keep the first slice list-only
- make the API base URL configurable for emulator and local development
- treat empty and error states differently in the UI
