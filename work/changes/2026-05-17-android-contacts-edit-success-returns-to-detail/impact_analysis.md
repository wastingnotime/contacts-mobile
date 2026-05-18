# Android Contacts Edit Success Returns To Detail Impact Analysis

## Slice Intent

Change the post-edit navigation policy so a successful edit returns the user to the selected contact detail.

## Why This Slice Now

The edit flow already has a success state. The remaining pressure is where the app should land after success.

## Impacted Boundaries

- interface layer: edit success surface and navigation resolution
- application layer: no change
- infrastructure layer: no change

## Model Pressure

The detail screen should remain the canonical place to inspect the edited contact after success.

This slice should preserve:

- edit validation
- edit request/response mapping
- backend failure handling

## Build Guidance

Keep the edit success surface transient. Do not add a separate permanent destination or alter the BFF edit contract.
