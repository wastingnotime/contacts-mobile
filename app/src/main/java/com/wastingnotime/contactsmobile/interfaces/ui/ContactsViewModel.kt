package com.wastingnotime.contactsmobile.interfaces.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.wastingnotime.contactsmobile.application.LoadContacts
import com.wastingnotime.contactsmobile.application.LoadContactsResult
import com.wastingnotime.contactsmobile.domain.Contact
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ContactsViewModel(
    private val loadContacts: LoadContacts,
) : ViewModel() {
    private val _uiState = MutableStateFlow<ContactsUiState>(ContactsUiState.Loading)
    val uiState: StateFlow<ContactsUiState> = _uiState.asStateFlow()

    private val _selectedContact = MutableStateFlow<Contact?>(null)
    val selectedContact: StateFlow<Contact?> = _selectedContact.asStateFlow()

    init {
        refresh()
    }

    fun refresh() {
        viewModelScope.launch {
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
    }

    fun openContact(contact: Contact) {
        _selectedContact.value = contact
    }

    fun closeContactDetail() {
        _selectedContact.value = null
    }
}

class ContactsViewModelFactory(
    private val loadContacts: LoadContacts,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ContactsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ContactsViewModel(loadContacts) as T
        }
        throw IllegalArgumentException("Unsupported view model class: ${modelClass.name}")
    }
}
