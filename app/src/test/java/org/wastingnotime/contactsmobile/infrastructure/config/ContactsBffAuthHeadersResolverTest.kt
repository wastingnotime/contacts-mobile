package org.wastingnotime.contactsmobile.infrastructure.config

import org.junit.Assert.assertEquals
import org.junit.Assert.fail
import org.junit.Test

class ContactsBffAuthHeadersResolverTest {
    @Test
    fun `resolves default admin claims`() {
        val headers = ContactsBffAuthHeadersResolver.resolve(
            ContactsBffAuthConfiguration(
                subject = "admin-user",
                roles = "admin",
            ),
        )

        assertEquals(
            ContactsBffAuthHeaders(
                subject = "admin-user",
                roles = "admin",
            ),
            headers,
        )
    }

    @Test
    fun `fails when the subject is blank`() {
        try {
            ContactsBffAuthHeadersResolver.resolve(
                ContactsBffAuthConfiguration(
                    subject = "   ",
                    roles = "admin",
                ),
            )
            fail("Expected subject validation to fail.")
        } catch (exception: IllegalArgumentException) {
            assertEquals("contactsBffAuthSubject must not be blank.", exception.message)
        }
    }

    @Test
    fun `fails when the roles are blank`() {
        try {
            ContactsBffAuthHeadersResolver.resolve(
                ContactsBffAuthConfiguration(
                    subject = "admin-user",
                    roles = "   ",
                ),
            )
            fail("Expected roles validation to fail.")
        } catch (exception: IllegalArgumentException) {
            assertEquals("contactsBffAuthRoles must not be blank.", exception.message)
        }
    }
}
