# Impact Analysis: Android Contacts Create Form IME Focus Progression

## Summary

The current create form already exists and submits the create-contact use case. The remaining gap is purely ergonomic: keyboard navigation does not yet guide the user through the required fields in order.

This slice keeps the backend contract unchanged and targets the interface layer only.

## Impacted Areas

- interface layer: create form composables and keyboard actions
- interface layer tests: create-form interaction coverage
- no change to application use cases or API transport

## Model Pressure

The model already accepts create-contact as a local form plus backend submission flow. The pressure here is to make the local interaction match the field ordering already implied by the form:

- first name
- last name
- phone number

The current form is valid but not yet optimized for keyboard-driven input. This slice keeps the data model and submission path intact while refining how users move through the fields.

## Boundary Notes

- do not change create success navigation here
- do not add a confirmation step
- do not alter validation requirements
- do not modify the contacts BFF client

## Suggested Verification

- deterministic UI or composable test that exercises keyboard progression across the three fields
- existing create validation tests should remain green
- existing create submission behavior should remain green
