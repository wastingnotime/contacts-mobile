package org.wastingnotime.contactsmobile.application

import org.wastingnotime.contactsmobile.domain.Contact

interface ContactsRepository {
    suspend fun loadContacts(): List<Contact>

    suspend fun loadContactById(id: String): Contact?

    suspend fun createContact(
        firstName: String,
        lastName: String,
        phoneNumber: String,
    ): Contact

    suspend fun updateContact(
        id: String,
        firstName: String,
        lastName: String,
        phoneNumber: String,
    ): Contact
}
