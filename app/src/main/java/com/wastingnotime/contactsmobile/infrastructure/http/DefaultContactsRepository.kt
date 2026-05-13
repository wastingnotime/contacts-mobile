package com.wastingnotime.contactsmobile.infrastructure.http

import com.wastingnotime.contactsmobile.application.ContactsRepository
import com.wastingnotime.contactsmobile.domain.Contact

class DefaultContactsRepository(
    private val apiClient: ContactsApiClient,
) : ContactsRepository {
    override suspend fun loadContacts(): List<Contact> {
        return apiClient.fetchContacts().map { it.toDomain() }
    }
}
