package com.wastingnotime.contactsmobile.infrastructure.config

import org.junit.Assert.assertEquals
import org.junit.Assert.fail
import org.junit.Test

class ContactsApiBaseUrlResolverTest {
    @Test
    fun `resolves the emulator base url`() {
        val baseUrl = ContactsApiBaseUrlResolver.resolve(
            ContactsApiBaseUrlConfiguration(
                environment = "emulator",
                emulatorBaseUrl = "http://10.0.2.2:8010",
                localDeviceBaseUrl = "http://127.0.0.1:8010",
                productionBaseUrl = "https://contacts.example.com",
            ),
        )

        assertEquals("http://10.0.2.2:8010", baseUrl)
    }

    @Test
    fun `resolves the local device base url`() {
        val baseUrl = ContactsApiBaseUrlResolver.resolve(
            ContactsApiBaseUrlConfiguration(
                environment = "local_device",
                emulatorBaseUrl = "http://10.0.2.2:8010",
                localDeviceBaseUrl = "http://192.168.1.40:8010",
                productionBaseUrl = "https://contacts.example.com",
            ),
        )

        assertEquals("http://192.168.1.40:8010", baseUrl)
    }

    @Test
    fun `requires a configured production base url`() {
        try {
            ContactsApiBaseUrlResolver.resolve(
                ContactsApiBaseUrlConfiguration(
                    environment = "production",
                    emulatorBaseUrl = "http://10.0.2.2:8010",
                    localDeviceBaseUrl = "http://127.0.0.1:8010",
                    productionBaseUrl = "   ",
                ),
            )
            fail("Expected production base url validation to fail.")
        } catch (exception: IllegalArgumentException) {
            assertEquals(
                "contactsApiProductionBaseUrl must be configured when contactsApiEnvironment=production.",
                exception.message,
            )
        }
    }
}
