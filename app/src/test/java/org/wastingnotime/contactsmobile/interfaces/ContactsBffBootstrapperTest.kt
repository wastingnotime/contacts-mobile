package org.wastingnotime.contactsmobile.interfaces

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.fail
import org.junit.Test

class ContactsBootstrapperTest {
    @Test
    fun `builds a view model factory from valid bootstrap configuration`() {
        val bootstrap = ContactsBffBootstrapper.build(
            ContactsBootstrapConfiguration(
                environment = "emulator",
                emulatorBaseUrl = "http://10.0.2.2:8010",
                localDeviceBaseUrl = "http://127.0.0.1:8010",
                productionBaseUrl = "https://contacts.example.com",
                authSubject = "admin-user",
                authRoles = "admin",
                apiPrefix = "/api",
            ),
        )

        assertNotNull(bootstrap.viewModelFactory)
    }

    @Test
    fun `fails when the api prefix is blank`() {
        try {
            ContactsBffBootstrapper.build(
                ContactsBootstrapConfiguration(
                    environment = "emulator",
                    emulatorBaseUrl = "http://10.0.2.2:8010",
                    localDeviceBaseUrl = "http://127.0.0.1:8010",
                    productionBaseUrl = "https://contacts.example.com",
                    authSubject = "admin-user",
                    authRoles = "admin",
                    apiPrefix = "   ",
                ),
            )
            fail("Expected bootstrap validation to fail.")
        } catch (exception: IllegalArgumentException) {
            assertEquals("contactsBffApiPrefix must not be blank.", exception.message)
        }
    }
}
