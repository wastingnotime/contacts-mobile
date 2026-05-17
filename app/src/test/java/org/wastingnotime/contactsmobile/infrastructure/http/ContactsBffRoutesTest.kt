package org.wastingnotime.contactsmobile.infrastructure.http

import org.junit.Assert.assertEquals
import org.junit.Test

class ContactsBffRoutesTest {
    @Test
    fun `resolves the list route beneath api`() {
        assertEquals("/api/contacts", ContactsBffRoutes.contactsList())
    }

    @Test
    fun `resolves the detail route beneath api`() {
        assertEquals("/api/contacts/contact-1", ContactsBffRoutes.contactById("contact-1"))
    }

    @Test
    fun `encodes detail path segments`() {
        assertEquals("/api/contacts/contact+1", ContactsBffRoutes.contactById("contact 1"))
    }

    @Test
    fun `reuses the list route for create`() {
        assertEquals("/api/contacts", ContactsBffRoutes.createContact())
    }

    @Test
    fun `reuses the detail route for update and delete`() {
        assertEquals("/api/contacts/contact-1", ContactsBffRoutes.updateContact("contact-1"))
        assertEquals("/api/contacts/contact-1", ContactsBffRoutes.deleteContact("contact-1"))
    }
}
