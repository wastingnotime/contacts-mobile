package com.wastingnotime.contactsmobile.application

import com.wastingnotime.contactsmobile.domain.Contact

interface ContactsRepository {
    suspend fun loadContacts(): List<Contact>

    suspend fun loadContactById(id: String): Contact?
}
