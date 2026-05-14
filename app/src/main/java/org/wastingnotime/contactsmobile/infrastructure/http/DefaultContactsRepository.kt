package org.wastingnotime.contactsmobile.infrastructure.http

import org.wastingnotime.contactsmobile.application.ContactsRepository
import org.wastingnotime.contactsmobile.domain.Contact

class DefaultContactsRepository(
    private val apiClient: ContactsApiClient,
) : ContactsRepository {
    override suspend fun loadContacts(): List<Contact> {
        return apiClient.fetchContacts().map { it.toDomain() }
    }

    override suspend fun loadContactById(id: String): Contact? {
        return apiClient.fetchContactById(id)?.toDomain()
    }

    override suspend fun createContact(
        firstName: String,
        lastName: String,
        phoneNumber: String,
    ): Contact {
        return apiClient.createContact(
            firstName = firstName,
            lastName = lastName,
            phoneNumber = phoneNumber,
        ).toDomain()
    }

    override suspend fun updateContact(
        id: String,
        firstName: String,
        lastName: String,
        phoneNumber: String,
    ): Contact {
        return apiClient.updateContact(
            id = id,
            firstName = firstName,
            lastName = lastName,
            phoneNumber = phoneNumber,
        ).toDomain()
    }

    override suspend fun deleteContact(id: String) {
        apiClient.deleteContact(id)
    }
}
