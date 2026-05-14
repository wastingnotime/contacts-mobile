package org.wastingnotime.contactsmobile.interfaces.ui

import org.wastingnotime.contactsmobile.domain.Contact

sealed interface ContactDetailUiState {
    data object Hidden : ContactDetailUiState

    data class Loading(val contactId: String) : ContactDetailUiState

    data class Loaded(
        val contact: Contact,
        val transientErrorMessage: String? = null,
        val freshnessState: ContactsFreshnessState = ContactsFreshnessState.Fresh,
        val staleAcknowledged: Boolean = false,
    ) : ContactDetailUiState

    data class Deleting(val contact: Contact) : ContactDetailUiState

    data class NotFound(val contactId: String) : ContactDetailUiState

    data class Error(
        val contactId: String,
        val message: String,
    ) : ContactDetailUiState
}
