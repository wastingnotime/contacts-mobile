package com.wastingnotime.contactsmobile.infrastructure.http

interface ContactsApiClient {
    suspend fun fetchContacts(): List<RemoteContact>
}
