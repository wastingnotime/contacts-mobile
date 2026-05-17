package org.wastingnotime.contactsmobile.infrastructure.config

import org.junit.Assert.assertEquals
import org.junit.Assert.fail
import org.junit.Test

class ContactsBffBaseUrlResolverTest {
    @Test
    fun `resolves the emulator base url`() {
        val baseUrl = ContactsBffBaseUrlResolver.resolve(
            ContactsBffBaseUrlConfiguration(
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
        val baseUrl = ContactsBffBaseUrlResolver.resolve(
            ContactsBffBaseUrlConfiguration(
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
            ContactsBffBaseUrlResolver.resolve(
                ContactsBffBaseUrlConfiguration(
                    environment = "production",
                    emulatorBaseUrl = "http://10.0.2.2:8010",
                    localDeviceBaseUrl = "http://127.0.0.1:8010",
                    productionBaseUrl = "   ",
                ),
            )
            fail("Expected production base url validation to fail.")
        } catch (exception: IllegalArgumentException) {
            assertEquals(
                "contactsBffProductionBaseUrl must be configured when contactsBffEnvironment=production.",
                exception.message,
            )
        }
    }
}
