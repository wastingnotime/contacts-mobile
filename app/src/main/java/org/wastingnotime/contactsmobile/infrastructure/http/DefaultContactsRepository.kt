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
}
