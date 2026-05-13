package com.wastingnotime.contactsmobile.interfaces.ui

import com.wastingnotime.contactsmobile.domain.Contact

sealed interface ContactsUiState {
    data object Loading : ContactsUiState

    data object Empty : ContactsUiState

    data class Loaded(val contacts: List<Contact>) : ContactsUiState

    data class Error(val message: String) : ContactsUiState
}
