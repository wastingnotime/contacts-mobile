# Exposure: Android Contacts Search Viewport Tracks Filtered Results

## Exposure Target

Expose the accepted search-viewport slice through a real Android client session:

- run the app on an emulator or local device
- load contacts from the configured backend
- enter a search query that narrows the loaded list
- scroll the filtered list away from the top
- change the query again and verify the viewport remains near the same visible neighborhood when possible
- clear the query and verify the full sorted list is still restored

## Expected Feedback Channels

- direct manual observation in Android Studio or on the device/emulator
- smoke-test notes from the manual session
- any mismatch between the remembered viewport and the visible filtered result set

## Scope Boundary

- this exposure does not change app behavior
- this exposure does not own long-term emulator operations
- backend simulation, if needed, should continue to come from the adjacent runtime sandbox workflow rather than this repository

## Status

- release accepted
- exposure plan recorded
