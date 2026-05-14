package org.wastingnotime.contactsmobile.infrastructure.http

interface ContactsApiClient {
    suspend fun fetchContacts(): List<RemoteContact>

    suspend fun fetchContactById(id: String): RemoteContact?

    suspend fun createContact(
        firstName: String,
        lastName: String,
        phoneNumber: String,
    ): RemoteContact
}
