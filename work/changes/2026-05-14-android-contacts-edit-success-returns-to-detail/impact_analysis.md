# Impact Analysis: Android Contacts Edit Success Returns To Detail

## Summary

The current edit flow leaves the user on a success surface with two actions. That adds an unnecessary navigation fork after a successful update.

This slice keeps edit submission intact and changes only the post-success navigation policy so the app returns to the contact detail after the success state is acknowledged.

## Impacted Areas

- interface layer: edit success surface and its navigation wiring
- interface layer tests: edit success and detail visibility coverage
- no change to application use cases or API transport

## Model Pressure

The app already applies the updated contact to local state after update succeeds. The remaining pressure is UX coherence:

- the detail already represents the selected contact
- a separate view-contact step duplicates the navigation path already available from detail
- returning to detail keeps the edit flow simple and preserves the edited record in its natural surface

This slice treats edit success as confirmation, not as a second navigation fork.

## Boundary Notes

- do not change edit validation
- do not change edit transport
- do not change edit failure handling
- do not edit the create-success policy here
- do not fold delete confirmation into this slice

## Suggested Verification

- deterministic UI or composable test that confirms edit success returns to detail
- test that the edited contact stays visible in detail after success
- existing edit transport and validation tests should remain green
