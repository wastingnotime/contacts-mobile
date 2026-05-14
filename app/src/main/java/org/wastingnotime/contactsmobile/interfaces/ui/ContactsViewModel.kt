package org.wastingnotime.contactsmobile.interfaces.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import org.wastingnotime.contactsmobile.application.CreateContact
import org.wastingnotime.contactsmobile.application.CreateContactCommand
import org.wastingnotime.contactsmobile.application.CreateContactResult
import org.wastingnotime.contactsmobile.application.DeleteContact
import org.wastingnotime.contactsmobile.application.DeleteContactResult
import org.wastingnotime.contactsmobile.application.FilterContacts
import org.wastingnotime.contactsmobile.application.LoadContactById
import org.wastingnotime.contactsmobile.application.LoadContactByIdResult
import org.wastingnotime.contactsmobile.application.LoadContacts
import org.wastingnotime.contactsmobile.application.LoadContactsResult
import org.wastingnotime.contactsmobile.application.SortContacts
import org.wastingnotime.contactsmobile.application.UpdateContact
import org.wastingnotime.contactsmobile.application.UpdateContactCommand
import org.wastingnotime.contactsmobile.application.UpdateContactResult
import org.wastingnotime.contactsmobile.domain.Contact
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ContactsViewModel(
    private val loadContacts: LoadContacts,
    private val loadContactById: LoadContactById,
    private val createContact: CreateContact,
    private val updateContact: UpdateContact,
    private val deleteContact: DeleteContact,
) : ViewModel() {
    private val filterContacts = FilterContacts()
    private val sortContacts = SortContacts()

    constructor(
        loadContacts: LoadContacts,
        loadContactById: LoadContactById,
        createContact: CreateContact,
        updateContact: UpdateContact,
    ) : this(
        loadContacts = loadContacts,
        loadContactById = loadContactById,
        createContact = createContact,
        updateContact = updateContact,
        deleteContact = DeleteContact(NoOpContactsRepository),
    )

    private val _uiState = MutableStateFlow<ContactsUiState>(ContactsUiState.Loading)
    val uiState: StateFlow<ContactsUiState> = _uiState.asStateFlow()

    private val _detailUiState = MutableStateFlow<ContactDetailUiState>(ContactDetailUiState.Hidden)
    val detailUiState: StateFlow<ContactDetailUiState> = _detailUiState.asStateFlow()

    private val _createUiState = MutableStateFlow<CreateContactUiState>(CreateContactUiState.Hidden)
    val createUiState: StateFlow<CreateContactUiState> = _createUiState.asStateFlow()

    private val _editUiState = MutableStateFlow<EditContactUiState>(EditContactUiState.Hidden)
    val editUiState: StateFlow<EditContactUiState> = _editUiState.asStateFlow()

    private var allContacts: List<Contact> = emptyList()
    private var searchQuery: String = ""

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

    fun updateSearchQuery(query: String) {
        searchQuery = query
        publishContactsState()
    }

    fun dismissContactsStaleIndicator() {
        val current = _uiState.value as? ContactsUiState.Loaded ?: return
        _uiState.value = current.copy(staleAcknowledged = true)
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

    fun openEditContact() {
        val currentDetail = _detailUiState.value as? ContactDetailUiState.Loaded ?: return
        _editUiState.value = EditContactUiState.Form(
            EditContactFormState(
                contactId = currentDetail.contact.id,
                firstName = currentDetail.contact.firstName,
                lastName = currentDetail.contact.lastName,
                phoneNumber = currentDetail.contact.phoneNumber,
            ),
        )
    }

    fun closeEditContact() {
        _editUiState.value = EditContactUiState.Hidden
    }

    fun updateEditFirstName(firstName: String) {
        updateEditForm { it.copy(firstName = firstName) }
    }

    fun updateEditLastName(lastName: String) {
        updateEditForm { it.copy(lastName = lastName) }
    }

    fun updateEditPhoneNumber(phoneNumber: String) {
        updateEditForm { it.copy(phoneNumber = phoneNumber) }
    }

    fun submitEditContact() {
        val current = _editUiState.value as? EditContactUiState.Form ?: return
        val validationErrors = validateEditForm(current.form)
        if (validationErrors.isNotEmpty()) {
            _editUiState.value = current.copy(
                form = current.form.copy(
                    fieldErrors = validationErrors,
                    errorMessage = "Please complete the required fields.",
                ),
            )
            return
        }

        viewModelScope.launch {
            _editUiState.value = current.copy(
                form = current.form.copy(
                    isSubmitting = true,
                    errorMessage = null,
                    fieldErrors = emptyMap(),
                ),
            )
            _editUiState.value = try {
                when (val result = updateContact.execute(
                    UpdateContactCommand(
                        id = current.form.contactId,
                        firstName = current.form.firstName.trim(),
                        lastName = current.form.lastName.trim(),
                        phoneNumber = current.form.phoneNumber.trim(),
                    ),
                )) {
                    is UpdateContactResult.Updated -> {
                        applyUpdatedContact(result.contact)
                        EditContactUiState.Success(result.contact)
                    }
                }
            } catch (exception: Throwable) {
                current.copy(
                    form = current.form.copy(
                        isSubmitting = false,
                        errorMessage = exception.message ?: "Unable to update contact.",
                    ),
                )
            }
        }
    }

    fun openUpdatedContact() {
        val successState = _editUiState.value as? EditContactUiState.Success ?: return
        _editUiState.value = EditContactUiState.Hidden
        loadContactDetail(successState.contact.id)
    }

    fun deleteContact() {
        val currentDetail = _detailUiState.value as? ContactDetailUiState.Loaded ?: return
        _detailUiState.value = ContactDetailUiState.Deleting(currentDetail.contact)
        viewModelScope.launch {
            _detailUiState.value = try {
                when (deleteContact.execute(currentDetail.contact.id)) {
                    DeleteContactResult.Deleted -> {
                        removeDeletedContactFromList(currentDetail.contact.id)
                        ContactDetailUiState.Hidden
                    }
                }
            } catch (exception: Throwable) {
                ContactDetailUiState.Loaded(
                    contact = currentDetail.contact,
                    transientErrorMessage = exception.message ?: "Unable to delete contact.",
                    freshnessState = ContactsFreshnessState.Stale,
                )
            }
        }
    }

    fun closeContactDetail() {
        _detailUiState.value = ContactDetailUiState.Hidden
    }

    fun dismissContactDetailStaleIndicator() {
        val current = _detailUiState.value as? ContactDetailUiState.Loaded ?: return
        _detailUiState.value = current.copy(staleAcknowledged = true)
    }

    private suspend fun refreshContacts() {
        val previousState = _uiState.value
        _uiState.value = ContactsUiState.Loading
        _uiState.value = try {
            when (val result = loadContacts.execute()) {
                LoadContactsResult.Empty -> {
                    allContacts = emptyList()
                    publishContactsState()
                }
                is LoadContactsResult.Loaded -> {
                    allContacts = result.contacts
                    publishContactsState()
                }
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
            is ContactDetailUiState.Deleting -> detailState.contact.id
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
            is ContactsUiState.Loaded -> previousState.copy(
                transientErrorMessage = message,
                freshnessState = ContactsFreshnessState.Stale,
                staleAcknowledged = false,
            )
            is ContactsUiState.Empty -> previousState.copy(transientErrorMessage = message)
            is ContactsUiState.FilteredEmpty -> previousState.copy(transientErrorMessage = message)
            else -> ContactsUiState.Error(message)
        }
    }

    private fun preserveContactDetailOnFailure(
        previousState: ContactDetailUiState,
        contactId: String,
        message: String,
    ): ContactDetailUiState {
        return when (previousState) {
            is ContactDetailUiState.Loaded -> previousState.copy(
                transientErrorMessage = message,
                freshnessState = ContactsFreshnessState.Stale,
                staleAcknowledged = false,
            )
            is ContactDetailUiState.Deleting -> ContactDetailUiState.Loaded(
                contact = previousState.contact,
                transientErrorMessage = message,
                freshnessState = ContactsFreshnessState.Stale,
                staleAcknowledged = false,
            )
            else -> ContactDetailUiState.Error(contactId, message)
        }
    }

    private fun updateCreateForm(transform: (CreateContactFormState) -> CreateContactFormState) {
        val current = _createUiState.value
        if (current is CreateContactUiState.Form) {
            _createUiState.value = current.copy(form = transform(current.form))
        }
    }

    private fun updateEditForm(transform: (EditContactFormState) -> EditContactFormState) {
        val current = _editUiState.value
        if (current is EditContactUiState.Form) {
            _editUiState.value = current.copy(form = transform(current.form))
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
        allContacts = sortContacts.execute(listOf(contact) + allContacts.filterNot { it.id == contact.id })
        publishContactsState()
    }

    private fun validateEditForm(form: EditContactFormState): Map<EditContactField, String> {
        val errors = mutableMapOf<EditContactField, String>()
        if (form.firstName.isBlank()) {
            errors[EditContactField.FirstName] = "First name is required."
        }
        if (form.lastName.isBlank()) {
            errors[EditContactField.LastName] = "Last name is required."
        }
        if (form.phoneNumber.isBlank()) {
            errors[EditContactField.PhoneNumber] = "Phone number is required."
        }
        return errors
    }

    private fun applyUpdatedContact(contact: Contact) {
        allContacts = sortContacts.execute(allContacts.map { existing ->
            if (existing.id == contact.id) contact else existing
        })
        publishContactsState()
        _detailUiState.value = ContactDetailUiState.Loaded(contact)
    }

    private fun removeDeletedContactFromList(contactId: String) {
        allContacts = sortContacts.execute(allContacts.filterNot { it.id == contactId })
        publishContactsState()
    }

    private fun publishContactsState(transientErrorMessage: String? = null): ContactsUiState {
        val filteredContacts = filterContacts.execute(allContacts, searchQuery)
        val nextState = when {
            allContacts.isEmpty() -> ContactsUiState.Empty(transientErrorMessage)
            searchQuery.isBlank() -> ContactsUiState.Loaded(
                contacts = sortContacts.execute(allContacts),
                transientErrorMessage = transientErrorMessage,
                freshnessState = ContactsFreshnessState.Fresh,
                staleAcknowledged = false,
            )
            filteredContacts.isEmpty() -> ContactsUiState.FilteredEmpty(
                query = searchQuery,
                transientErrorMessage = transientErrorMessage,
            )
            else -> ContactsUiState.Loaded(
                contacts = sortContacts.execute(filteredContacts),
                transientErrorMessage = transientErrorMessage,
                searchQuery = searchQuery,
                freshnessState = ContactsFreshnessState.Fresh,
                staleAcknowledged = false,
            )
        }
        _uiState.value = nextState
        return nextState
    }
}

class ContactsViewModelFactory(
    private val loadContacts: LoadContacts,
    private val loadContactById: LoadContactById,
    private val createContact: CreateContact,
    private val updateContact: UpdateContact,
    private val deleteContact: DeleteContact,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ContactsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ContactsViewModel(loadContacts, loadContactById, createContact, updateContact, deleteContact) as T
        }
        throw IllegalArgumentException("Unsupported view model class: ${modelClass.name}")
    }
}

private object NoOpContactsRepository : org.wastingnotime.contactsmobile.application.ContactsRepository {
    override suspend fun loadContacts(): List<org.wastingnotime.contactsmobile.domain.Contact> = emptyList()

    override suspend fun loadContactById(id: String): org.wastingnotime.contactsmobile.domain.Contact? = null

    override suspend fun createContact(
        firstName: String,
        lastName: String,
        phoneNumber: String,
    ): org.wastingnotime.contactsmobile.domain.Contact {
        error("No-op repository does not support create.")
    }

    override suspend fun updateContact(
        id: String,
        firstName: String,
        lastName: String,
        phoneNumber: String,
    ): org.wastingnotime.contactsmobile.domain.Contact {
        error("No-op repository does not support update.")
    }

    override suspend fun deleteContact(id: String) {
        error("No-op repository does not support delete.")
    }
}
