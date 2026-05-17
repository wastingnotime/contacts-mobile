package org.wastingnotime.contactsmobile.interfaces

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test
import org.wastingnotime.contactsmobile.application.ContactsRepository
import org.wastingnotime.contactsmobile.domain.Contact

class ContactsBffUseCaseAssemblyTest {
    @Test
    fun `assembles the five contacts use cases from a repository`() {
        val useCases = ContactsBffUseCaseAssembly.assemble(NoOpContactsRepository)

        assertNotNull(useCases.loadContacts)
        assertNotNull(useCases.loadContactById)
        assertNotNull(useCases.createContact)
        assertNotNull(useCases.updateContact)
        assertNotNull(useCases.deleteContact)
        assertEquals("LoadContacts", useCases.loadContacts::class.simpleName)
        assertEquals("LoadContactById", useCases.loadContactById::class.simpleName)
        assertEquals("CreateContact", useCases.createContact::class.simpleName)
        assertEquals("UpdateContact", useCases.updateContact::class.simpleName)
        assertEquals("DeleteContact", useCases.deleteContact::class.simpleName)
    }
}

private object NoOpContactsRepository : ContactsRepository {
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
