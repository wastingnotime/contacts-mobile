# Android Contacts Create Success Returns To List Impact Analysis

## Slice Intent

Change the post-create navigation policy so a successful create returns the user to the contacts list.

## Why This Slice Now

The create flow already has a success state. The remaining pressure is where the app should land after success.

## Impacted Boundaries

- interface layer: create success surface and navigation resolution
- application layer: no change
- infrastructure layer: no change

## Model Pressure

The list should remain the canonical place to re-enter the created contact after success.

This slice should preserve:

- create validation
- create request/response mapping
- backend failure handling

## Build Guidance

Keep the create success surface transient. Do not add a separate permanent destination or alter the BFF create contract.
