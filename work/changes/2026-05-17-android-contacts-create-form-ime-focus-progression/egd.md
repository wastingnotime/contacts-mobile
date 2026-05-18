# Android Contacts Create Form IME Focus Progression EGD

## Slice

`docs/slices/android_contacts_create_form_ime_focus_progression.md`

## Evidence Reviewed

- `docs/semantics/model_hypothesis.md`
- `docs/semantics/domain_background_knowledge.md`
- `docs/slices/android_contacts_create_form_ime_focus_progression.md`
- `work/changes/2026-05-17-android-contacts-create-form-ime-focus-progression/impact_analysis.md`
- `work/changes/2026-05-17-android-contacts-create-form-ime-focus-progression/implementation.md`
- `app/src/main/java/org/wastingnotime/contactsmobile/interfaces/ui/ContactsScreen.kt`
- `app/src/androidTest/java/org/wastingnotime/contactsmobile/interfaces/ui/ContactsScreenCreateFormImeTest.kt`
- `./gradlew test`

## Summary

The implemented slice matches the refined intent. The create form now advances focus deterministically from first name to last name to phone number, and the final IME action submits the existing create flow.

The change stays inside the interface layer, preserves validation semantics, and does not alter create success navigation or transport mapping.

## Findings

### 1. IME progression is explicit and deterministic

Observed behavior:

- first name uses `ImeAction.Next` and moves focus to last name
- last name uses `ImeAction.Next` and moves focus to phone number
- phone number uses `ImeAction.Done` and submits the form
- the androidTest asserts each focus transition and the final submit callback

Assessment:

- this satisfies the slice intent directly
- there is no ambiguity in the progression order
- the behavior is covered at the UI boundary

### 2. Validation and navigation remain outside the slice boundary

Observed behavior:

- the slice does not add new validation rules
- create success handling still routes through the separate success-navigation slice
- the app still submits through the existing create flow

Assessment:

- this matches the refined impact analysis
- no hidden coupling was introduced between keyboard progression and post-submit navigation

### 3. No blocking expectation gap remains

Observed behavior:

- the create-form IME test passes
- the app build remains green

Assessment:

- the implemented behavior aligns with the model hypothesis
- no further refinement is required for this slice

## Review Questions

1. Should the edit form keep the same IME progression pattern, or is it intentionally allowed to differ?
2. Do we want to surface keyboard progression as a shared form helper later, or keep it local to each form?

## Recommendation

Continue to release.

Reasoning:

- the built behavior matches the slice intent
- the test coverage is direct and deterministic
- the remaining questions are future consistency choices, not defects
