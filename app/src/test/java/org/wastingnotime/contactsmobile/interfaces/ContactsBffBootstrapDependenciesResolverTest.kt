package org.wastingnotime.contactsmobile.interfaces

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test

class ContactsBootstrapDependenciesResolverTest {
    @Test
    fun `resolves api client and repository from valid bootstrap configuration`() {
        val dependencies = ContactsBootstrapDependenciesResolver.resolve(
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

        assertNotNull(dependencies.apiClient)
        assertNotNull(dependencies.repository)
        assertEquals("HttpContactsBffClient", dependencies.apiClient::class.simpleName)
        assertEquals("DefaultContactsRepository", dependencies.repository::class.simpleName)
    }
}
