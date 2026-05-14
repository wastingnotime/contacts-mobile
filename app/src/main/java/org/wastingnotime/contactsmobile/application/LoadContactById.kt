package org.wastingnotime.contactsmobile.application

import org.wastingnotime.contactsmobile.domain.Contact

sealed interface LoadContactByIdResult {
    data object NotFound : LoadContactByIdResult

    data class Loaded(val contact: Contact) : LoadContactByIdResult
}

class LoadContactById(
    private val repository: ContactsRepository,
) {
    suspend fun execute(id: String): LoadContactByIdResult {
        val contact = repository.loadContactById(id)
            ?: return LoadContactByIdResult.NotFound
        return LoadContactByIdResult.Loaded(contact)
    }
}
