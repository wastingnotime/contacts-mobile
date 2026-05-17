package org.wastingnotime.contactsmobile.interfaces

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test

class ContactsAppStartTest {
    @Test
    fun `starts the app from an explicit bootstrap configuration`() {
        val bootstrap = ContactsAppStart.start(
            ContactsBffBootstrapConfiguration(
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
        assertEquals("ContactsViewModelFactory", bootstrap.viewModelFactory::class.simpleName)
    }
}
