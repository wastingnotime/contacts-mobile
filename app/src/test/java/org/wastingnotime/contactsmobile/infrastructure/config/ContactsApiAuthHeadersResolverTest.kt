package org.wastingnotime.contactsmobile.infrastructure.config

import org.junit.Assert.assertEquals
import org.junit.Assert.fail
import org.junit.Test

class ContactsApiAuthHeadersResolverTest {
    @Test
    fun `resolves default admin claims`() {
        val headers = ContactsApiAuthHeadersResolver.resolve(
            ContactsApiAuthConfiguration(
                subject = "admin-user",
                roles = "admin",
            ),
        )

        assertEquals(
            ContactsApiAuthHeaders(
                subject = "admin-user",
                roles = "admin",
            ),
            headers,
        )
    }

    @Test
    fun `fails when the subject is blank`() {
        try {
            ContactsApiAuthHeadersResolver.resolve(
                ContactsApiAuthConfiguration(
                    subject = "   ",
                    roles = "admin",
                ),
            )
            fail("Expected subject validation to fail.")
        } catch (exception: IllegalArgumentException) {
            assertEquals("contactsApiAuthSubject must not be blank.", exception.message)
        }
    }

    @Test
    fun `fails when the roles are blank`() {
        try {
            ContactsApiAuthHeadersResolver.resolve(
                ContactsApiAuthConfiguration(
                    subject = "admin-user",
                    roles = "   ",
                ),
            )
            fail("Expected roles validation to fail.")
        } catch (exception: IllegalArgumentException) {
            assertEquals("contactsApiAuthRoles must not be blank.", exception.message)
        }
    }
}
