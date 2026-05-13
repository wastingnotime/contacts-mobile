package com.wastingnotime.contactsmobile.interfaces.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.wastingnotime.contactsmobile.application.LoadContactById
import com.wastingnotime.contactsmobile.application.LoadContactByIdResult
import com.wastingnotime.contactsmobile.application.LoadContacts
import com.wastingnotime.contactsmobile.application.LoadContactsResult
import com.wastingnotime.contactsmobile.domain.Contact
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ContactsViewModel(
    private val loadContacts: LoadContacts,
    private val loadContactById: LoadContactById,
) : ViewModel() {
    private val _uiState = MutableStateFlow<ContactsUiState>(ContactsUiState.Loading)
    val uiState: StateFlow<ContactsUiState> = _uiState.asStateFlow()

    private val _detailUiState = MutableStateFlow<ContactDetailUiState>(ContactDetailUiState.Hidden)
    val detailUiState: StateFlow<ContactDetailUiState> = _detailUiState.asStateFlow()

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

    fun closeContactDetail() {
        _detailUiState.value = ContactDetailUiState.Hidden
    }

    private suspend fun refreshContacts() {
        _uiState.value = ContactsUiState.Loading
        _uiState.value = try {
            when (val result = loadContacts.execute()) {
                LoadContactsResult.Empty -> ContactsUiState.Empty
                is LoadContactsResult.Loaded -> ContactsUiState.Loaded(result.contacts)
            }
        } catch (exception: Throwable) {
            ContactsUiState.Error(exception.message ?: "Unable to load contacts.")
        }
    }

    private fun loadContactDetail(contactId: String) {
        _detailUiState.value = ContactDetailUiState.Loading(contactId)
        viewModelScope.launch {
            _detailUiState.value = try {
                when (val result = loadContactById.execute(contactId)) {
                    is LoadContactByIdResult.Loaded -> ContactDetailUiState.Loaded(result.contact)
                    LoadContactByIdResult.NotFound -> ContactDetailUiState.NotFound(contactId)
                }
            } catch (exception: Throwable) {
                ContactDetailUiState.Error(contactId, exception.message ?: "Unable to load contact.")
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
}

class ContactsViewModelFactory(
    private val loadContacts: LoadContacts,
    private val loadContactById: LoadContactById,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ContactsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ContactsViewModel(loadContacts, loadContactById) as T
        }
        throw IllegalArgumentException("Unsupported view model class: ${modelClass.name}")
    }
}
