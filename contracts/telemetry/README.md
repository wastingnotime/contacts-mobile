# Telemetry Contract

## Scope

This contract describes what mobile telemetry the repository considers part of the public boundary.

## Stable Semantics

- telemetry semantics should be explicit and intentional
- telemetry should support understanding app behavior without exposing internal implementation details
- telemetry expectations should stay aligned with user-visible states and boundary events
- BFF runtime logs can be exported as OTLP logs through the shared collector when observability is enabled
- producer identity should remain explicit in the exported log resource and logger naming
- the observed runtime should remain explicit in the exported telemetry attributes

## Notes

Do not treat this as a place for build logs or internal debug noise.
