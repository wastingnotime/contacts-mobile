package org.wastingnotime.contactsmobile.interfaces.ui

import org.wastingnotime.contactsmobile.domain.Contact

data class EditContactFormState(
    val contactId: String = "",
    val firstName: String = "",
    val lastName: String = "",
    val phoneNumber: String = "",
    val isSubmitting: Boolean = false,
    val errorMessage: String? = null,
    val fieldErrors: Map<EditContactField, String> = emptyMap(),
)

enum class EditContactField {
    FirstName,
    LastName,
    PhoneNumber,
}

sealed interface EditContactUiState {
    data object Hidden : EditContactUiState

    data class Form(
        val form: EditContactFormState,
    ) : EditContactUiState

    data class Success(
        val contact: Contact,
    ) : EditContactUiState
}
