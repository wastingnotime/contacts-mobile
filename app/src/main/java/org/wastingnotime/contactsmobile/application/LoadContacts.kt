package org.wastingnotime.contactsmobile.application

import org.wastingnotime.contactsmobile.domain.Contact

sealed interface LoadContactsResult {
    data object Empty : LoadContactsResult

    data class Loaded(val contacts: List<Contact>) : LoadContactsResult
}

class LoadContacts(
    private val repository: ContactsRepository,
) {
    suspend fun execute(): LoadContactsResult {
        val contacts = repository.loadContacts()
        return if (contacts.isEmpty()) {
            LoadContactsResult.Empty
        } else {
            LoadContactsResult.Loaded(contacts)
        }
    }
}
