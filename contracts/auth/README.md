# Auth Contract

## Scope

This contract describes how the mobile client expects identity, session, and request claims to behave at the public boundary.

## Stable Semantics

- auth and session expectations are part of the exported contract
- request-claims style headers or similar boundary metadata should remain visible at the transport edge
- the mobile client should not hide auth semantics behind implicit internal state

## Notes

Any token format, claim shape, refresh policy, or header rule that affects the public boundary should be recorded here.
