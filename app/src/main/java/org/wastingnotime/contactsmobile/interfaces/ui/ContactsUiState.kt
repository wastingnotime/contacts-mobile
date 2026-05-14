package org.wastingnotime.contactsmobile.interfaces.ui

import org.wastingnotime.contactsmobile.domain.Contact

sealed interface ContactsUiState {
    data object Loading : ContactsUiState

    data class Empty(
        val transientErrorMessage: String? = null,
    ) : ContactsUiState

    data class Loaded(
        val contacts: List<Contact>,
        val transientErrorMessage: String? = null,
    ) : ContactsUiState

    data class Error(val message: String) : ContactsUiState
}
