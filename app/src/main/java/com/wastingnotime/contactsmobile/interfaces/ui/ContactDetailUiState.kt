package com.wastingnotime.contactsmobile.interfaces.ui

import com.wastingnotime.contactsmobile.domain.Contact

sealed interface ContactDetailUiState {
    data object Hidden : ContactDetailUiState

    data class Loading(val contactId: String) : ContactDetailUiState

    data class Loaded(val contact: Contact) : ContactDetailUiState

    data class NotFound(val contactId: String) : ContactDetailUiState

    data class Error(
        val contactId: String,
        val message: String,
    ) : ContactDetailUiState
}
