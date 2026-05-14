package org.wastingnotime.contactsmobile.application

import org.wastingnotime.contactsmobile.domain.Contact

interface ContactsRepository {
    suspend fun loadContacts(): List<Contact>

    suspend fun loadContactById(id: String): Contact?
}
