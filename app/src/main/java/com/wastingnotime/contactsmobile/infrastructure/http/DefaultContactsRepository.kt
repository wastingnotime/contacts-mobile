package com.wastingnotime.contactsmobile.infrastructure.http

import com.wastingnotime.contactsmobile.application.ContactsRepository
import com.wastingnotime.contactsmobile.domain.Contact

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
