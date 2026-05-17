package org.wastingnotime.contactsmobile.interfaces

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test

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
