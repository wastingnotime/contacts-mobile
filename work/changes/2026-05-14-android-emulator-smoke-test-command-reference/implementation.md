# Android Emulator Smoke Test Command Reference Implementation

## Slice

`docs/slices/android_emulator_smoke_test_command_reference.md`

## Implemented

- made the emulator smoke-test runbook command-level instead of generic
- named the runtime sandbox commands used to seed and start the backend smoke path
- added the client-side `./gradlew installDebug` step for emulator validation
- kept the guidance separate from runtime app behavior and backend ownership

## Validation

- `git diff --check` passed

## Notes

- the command reference intentionally points at `../runtime-sandbox` rather than duplicating backend simulation logic here
- the Android client repo only documents how to run the smoke path, not how to own the backend
