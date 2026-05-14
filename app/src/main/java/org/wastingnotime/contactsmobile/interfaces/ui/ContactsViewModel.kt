package org.wastingnotime.contactsmobile.interfaces.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import org.wastingnotime.contactsmobile.application.CreateContact
import org.wastingnotime.contactsmobile.application.CreateContactCommand
import org.wastingnotime.contactsmobile.application.CreateContactResult
import org.wastingnotime.contactsmobile.application.LoadContactById
import org.wastingnotime.contactsmobile.application.LoadContactByIdResult
import org.wastingnotime.contactsmobile.application.LoadContacts
import org.wastingnotime.contactsmobile.application.LoadContactsResult
import org.wastingnotime.contactsmobile.domain.Contact
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ContactsViewModel(
    private val loadContacts: LoadContacts,
    private val loadContactById: LoadContactById,
    private val createContact: CreateContact,
) : ViewModel() {
    private val _uiState = MutableStateFlow<ContactsUiState>(ContactsUiState.Loading)
    val uiState: StateFlow<ContactsUiState> = _uiState.asStateFlow()

    private val _detailUiState = MutableStateFlow<ContactDetailUiState>(ContactDetailUiState.Hidden)
    val detailUiState: StateFlow<ContactDetailUiState> = _detailUiState.asStateFlow()

    private val _createUiState = MutableStateFlow<CreateContactUiState>(CreateContactUiState.Hidden)
    val createUiState: StateFlow<CreateContactUiState> = _createUiState.asStateFlow()

    init {
        refresh()
    }

    fun refresh() {
        val detailState = _detailUiState.value
        if (detailState != ContactDetailUiState.Hidden) {
            refreshDetail(detailState)
            return
        }

        viewModelScope.launch {
            refreshContacts()
        }
    }

    fun openContact(contact: Contact) {
        loadContactDetail(contact.id)
    }

    fun openCreateContact() {
        _createUiState.value = CreateContactUiState.Form(CreateContactFormState())
    }

    fun closeCreateContact() {
        _createUiState.value = CreateContactUiState.Hidden
    }

    fun updateCreateFirstName(firstName: String) {
        updateCreateForm { it.copy(firstName = firstName) }
    }

    fun updateCreateLastName(lastName: String) {
        updateCreateForm { it.copy(lastName = lastName) }
    }

    fun updateCreatePhoneNumber(phoneNumber: String) {
        updateCreateForm { it.copy(phoneNumber = phoneNumber) }
    }

    fun submitCreateContact() {
        val current = _createUiState.value as? CreateContactUiState.Form ?: return
        val validationErrors = validateCreateForm(current.form)
        if (validationErrors.isNotEmpty()) {
            _createUiState.value = current.copy(
                form = current.form.copy(
                    fieldErrors = validationErrors,
                    errorMessage = "Please complete the required fields.",
                ),
            )
            return
        }

        viewModelScope.launch {
            _createUiState.value = current.copy(
                form = current.form.copy(
                    isSubmitting = true,
                    errorMessage = null,
                    fieldErrors = emptyMap(),
                ),
            )
            _createUiState.value = try {
                when (val result = createContact.execute(
                    CreateContactCommand(
                        firstName = current.form.firstName.trim(),
                        lastName = current.form.lastName.trim(),
                        phoneNumber = current.form.phoneNumber.trim(),
                    ),
                )) {
                    is CreateContactResult.Created -> {
                        insertCreatedContactIntoList(result.contact)
                        CreateContactUiState.Success(result.contact)
                    }
                }
            } catch (exception: Throwable) {
                current.copy(
                    form = current.form.copy(
                        isSubmitting = false,
                        errorMessage = exception.message ?: "Unable to create contact.",
                    ),
                )
            }
        }
    }

    fun openCreatedContact() {
        val successState = _createUiState.value as? CreateContactUiState.Success ?: return
        _createUiState.value = CreateContactUiState.Hidden
        loadContactDetail(successState.contact.id)
    }

    fun closeContactDetail() {
        _detailUiState.value = ContactDetailUiState.Hidden
    }

    private suspend fun refreshContacts() {
        val previousState = _uiState.value
        _uiState.value = ContactsUiState.Loading
        _uiState.value = try {
            when (val result = loadContacts.execute()) {
                LoadContactsResult.Empty -> ContactsUiState.Empty()
                is LoadContactsResult.Loaded -> ContactsUiState.Loaded(result.contacts)
            }
        } catch (exception: Throwable) {
            preserveContactsOnFailure(previousState, exception.message ?: "Unable to load contacts.")
        }
    }

    private fun loadContactDetail(contactId: String) {
        val previousState = _detailUiState.value
        _detailUiState.value = ContactDetailUiState.Loading(contactId)
        viewModelScope.launch {
            _detailUiState.value = try {
                when (val result = loadContactById.execute(contactId)) {
                    is LoadContactByIdResult.Loaded -> ContactDetailUiState.Loaded(result.contact)
                    LoadContactByIdResult.NotFound -> ContactDetailUiState.NotFound(contactId)
                }
            } catch (exception: Throwable) {
                preserveContactDetailOnFailure(
                    previousState = previousState,
                    contactId = contactId,
                    message = exception.message ?: "Unable to load contact.",
                )
            }
        }
    }

    private fun refreshDetail(detailState: ContactDetailUiState) {
        val contactId = when (detailState) {
            is ContactDetailUiState.Loading -> detailState.contactId
            is ContactDetailUiState.Loaded -> detailState.contact.id
            is ContactDetailUiState.NotFound -> detailState.contactId
            is ContactDetailUiState.Error -> detailState.contactId
            ContactDetailUiState.Hidden -> return
        }
        loadContactDetail(contactId)
    }

    private fun preserveContactsOnFailure(
        previousState: ContactsUiState,
        message: String,
    ): ContactsUiState {
        return when (previousState) {
            is ContactsUiState.Loaded -> previousState.copy(transientErrorMessage = message)
            is ContactsUiState.Empty -> previousState.copy(transientErrorMessage = message)
            else -> ContactsUiState.Error(message)
        }
    }

    private fun preserveContactDetailOnFailure(
        previousState: ContactDetailUiState,
        contactId: String,
        message: String,
    ): ContactDetailUiState {
        return when (previousState) {
            is ContactDetailUiState.Loaded -> previousState.copy(transientErrorMessage = message)
            else -> ContactDetailUiState.Error(contactId, message)
        }
    }

    private fun updateCreateForm(transform: (CreateContactFormState) -> CreateContactFormState) {
        val current = _createUiState.value
        if (current is CreateContactUiState.Form) {
            _createUiState.value = current.copy(form = transform(current.form))
        }
    }

    private fun validateCreateForm(form: CreateContactFormState): Map<CreateContactField, String> {
        val errors = mutableMapOf<CreateContactField, String>()
        if (form.firstName.isBlank()) {
            errors[CreateContactField.FirstName] = "First name is required."
        }
        if (form.lastName.isBlank()) {
            errors[CreateContactField.LastName] = "Last name is required."
        }
        if (form.phoneNumber.isBlank()) {
            errors[CreateContactField.PhoneNumber] = "Phone number is required."
        }
        return errors
    }

    private fun insertCreatedContactIntoList(contact: Contact) {
        _uiState.value = when (val current = _uiState.value) {
            is ContactsUiState.Loaded -> current.copy(
                contacts = listOf(contact) + current.contacts.filterNot { it.id == contact.id },
            )
            is ContactsUiState.Empty -> ContactsUiState.Loaded(listOf(contact))
            else -> current
        }
    }
}

class ContactsViewModelFactory(
    private val loadContacts: LoadContacts,
    private val loadContactById: LoadContactById,
    private val createContact: CreateContact,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ContactsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ContactsViewModel(loadContacts, loadContactById, createContact) as T
        }
        throw IllegalArgumentException("Unsupported view model class: ${modelClass.name}")
    }
}
