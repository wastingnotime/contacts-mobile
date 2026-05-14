# Impact Analysis: Android Contacts Delete Confirmation

## Summary

The current delete flow is destructive immediately from the detail surface. The gap is not about transport or repository behavior; it is about adding a confirmation gate before the existing `DeleteContact` use case runs.

This slice targets the interface layer only and keeps the current delete success/failure semantics intact.

## Impacted Areas

- interface layer: detail surface, confirmation UI, and delete trigger wiring
- interface layer tests: delete confirmation and cancellation coverage
- no change to application use cases or API transport

## Model Pressure

The repository already treats delete as a detail-surface action. The new pressure is to protect that action with an explicit user decision because deletion is irreversible from the app's point of view.

This is a UI contract change, not a backend contract change:

- the contact stays visible until confirmation
- confirmation is a separate step from execution
- the current delete success and failure outcomes remain valid after confirmation

## Boundary Notes

- do not change delete transport
- do not change delete success/failure semantics
- do not introduce a new domain object for confirmation
- do not fold create-success or edit-success navigation cleanup into this slice

## Suggested Verification

- deterministic UI or composable test that proves delete first shows confirmation
- test that cancellation leaves the detail surface intact
- test that confirmation still drives the existing delete execution path
