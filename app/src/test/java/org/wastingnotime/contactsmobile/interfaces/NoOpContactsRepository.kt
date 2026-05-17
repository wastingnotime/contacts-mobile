package org.wastingnotime.contactsmobile.interfaces

import org.wastingnotime.contactsmobile.application.ContactsRepository
import org.wastingnotime.contactsmobile.domain.Contact

object NoOpContactsRepository : ContactsRepository {
    override suspend fun loadContacts(): List<Contact> = emptyList()

    override suspend fun loadContactById(id: String): Contact? = null

    override suspend fun createContact(
        firstName: String,
        lastName: String,
        phoneNumber: String,
    ): Contact {
        error("No-op repository does not support create.")
    }

    override suspend fun updateContact(
        id: String,
        firstName: String,
        lastName: String,
        phoneNumber: String,
    ): Contact {
        error("No-op repository does not support update.")
    }

    override suspend fun deleteContact(id: String) {
        error("No-op repository does not support delete.")
    }
}
