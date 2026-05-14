package org.wastingnotime.contactsmobile.interfaces.ui

import org.wastingnotime.contactsmobile.domain.Contact

data class CreateContactFormState(
    val firstName: String = "",
    val lastName: String = "",
    val phoneNumber: String = "",
    val isSubmitting: Boolean = false,
    val errorMessage: String? = null,
    val fieldErrors: Map<CreateContactField, String> = emptyMap(),
)

enum class CreateContactField {
    FirstName,
    LastName,
    PhoneNumber,
}

sealed interface CreateContactUiState {
    data object Hidden : CreateContactUiState

    data class Form(
        val form: CreateContactFormState,
    ) : CreateContactUiState

    data class Success(
        val contact: Contact,
    ) : CreateContactUiState
}
