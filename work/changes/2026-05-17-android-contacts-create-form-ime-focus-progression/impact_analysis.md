# Android Contacts Create Form IME Focus Progression Impact Analysis

## Slice Intent

Make the create form keyboard-driven by moving focus through first name, last name, and phone number in a deterministic order.

## Why This Slice Now

The create flow already exists. The remaining pressure is form ergonomics, not backend behavior.

## Impacted Boundaries

- interface layer: create form keyboard actions and focus movement
- application layer: no change
- infrastructure layer: no change

## Model Pressure

The form should behave like a compact data-entry surface:

- next action moves to the next field
- the final field can submit the form
- validation semantics stay the same

## Build Guidance

Keep the slice focused on IME progression only. Do not change create success navigation, create transport, or validation rules.
