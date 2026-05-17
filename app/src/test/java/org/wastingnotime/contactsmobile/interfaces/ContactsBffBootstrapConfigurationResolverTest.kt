package org.wastingnotime.contactsmobile.interfaces

import org.junit.Assert.assertEquals
import org.junit.Assert.fail
import org.junit.Test

class ContactsBffBootstrapConfigurationResolverTest {
    @Test
    fun `resolves and normalizes valid bootstrap build configuration`() {
        val configuration = ContactsBffBootstrapConfigurationResolver.resolve(
            ContactsBffBootstrapBuildConfiguration(
                environment = " Emulator ",
                emulatorBaseUrl = " http://10.0.2.2:8010 ",
                localDeviceBaseUrl = " http://127.0.0.1:8010 ",
                productionBaseUrl = " https://contacts.example.com ",
                authSubject = " admin-user ",
                authRoles = " admin ",
                apiPrefix = " /api ",
            ),
        )

        assertEquals("emulator", configuration.environment)
        assertEquals("http://10.0.2.2:8010", configuration.emulatorBaseUrl)
        assertEquals("http://127.0.0.1:8010", configuration.localDeviceBaseUrl)
        assertEquals("https://contacts.example.com", configuration.productionBaseUrl)
        assertEquals("admin-user", configuration.authSubject)
        assertEquals("admin", configuration.authRoles)
        assertEquals("/api", configuration.apiPrefix)
    }

    @Test
    fun `fails when the api prefix is blank`() {
        try {
            ContactsBffBootstrapConfigurationResolver.resolve(
                ContactsBffBootstrapBuildConfiguration(
                    environment = "emulator",
                    emulatorBaseUrl = "http://10.0.2.2:8010",
                    localDeviceBaseUrl = "http://127.0.0.1:8010",
                    productionBaseUrl = "https://contacts.example.com",
                    authSubject = "admin-user",
                    authRoles = "admin",
                    apiPrefix = "   ",
                ),
            )
            fail("Expected bootstrap configuration resolution to fail.")
        } catch (exception: IllegalArgumentException) {
            assertEquals("contactsBffApiPrefix must not be blank.", exception.message)
        }
    }
}
