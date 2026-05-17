package org.wastingnotime.contactsmobile.infrastructure.http

import org.junit.Assert.assertEquals
import org.junit.Assert.fail
import org.junit.Test

class ContactsBffApiSurfaceTest {
    @Test
    fun `resolves the api surface beneath the configured prefix`() {
        val apiSurface = ContactsBffApiSurfaceResolver.resolve(
            ContactsBffApiSurfaceConfiguration(apiPrefix = "/api"),
        )

        assertEquals("/api/contacts", apiSurface.contactsList())
        assertEquals("/api/contacts/contact-1", apiSurface.contactById("contact-1"))
        assertEquals("/api/contacts", apiSurface.createContact())
        assertEquals("/api/contacts/contact-1", apiSurface.updateContact("contact-1"))
        assertEquals("/api/contacts/contact-1", apiSurface.deleteContact("contact-1"))
    }

    @Test
    fun `normalizes a prefix without a leading slash`() {
        val apiSurface = ContactsBffApiSurfaceResolver.resolve(
            ContactsBffApiSurfaceConfiguration(apiPrefix = "api"),
        )

        assertEquals("/api/contacts", apiSurface.contactsList())
    }

    @Test
    fun `rejects a blank api prefix`() {
        try {
            ContactsBffApiSurfaceResolver.resolve(
                ContactsBffApiSurfaceConfiguration(apiPrefix = "   "),
            )
            fail("Expected api prefix validation to fail.")
        } catch (exception: IllegalArgumentException) {
            assertEquals("contactsBffApiPrefix must not be blank.", exception.message)
        }
    }
}
