package org.wastingnotime.contactsmobile.interfaces

import org.junit.Assert.assertEquals
import org.junit.Test
import org.wastingnotime.contactsmobile.BuildConfig

class ContactsBffBootstrapBuildConfigurationSourceTest {
    @Test
    fun `resolves raw build configuration from build config values`() {
        val configuration = ContactsBffBootstrapBuildConfigurationSource.resolve()

        assertEquals(BuildConfig.CONTACTS_BFF_ENVIRONMENT, configuration.environment)
        assertEquals(BuildConfig.CONTACTS_BFF_EMULATOR_BASE_URL, configuration.emulatorBaseUrl)
        assertEquals(BuildConfig.CONTACTS_BFF_LOCAL_DEVICE_BASE_URL, configuration.localDeviceBaseUrl)
        assertEquals(BuildConfig.CONTACTS_BFF_PRODUCTION_BASE_URL, configuration.productionBaseUrl)
        assertEquals(BuildConfig.CONTACTS_BFF_AUTH_SUBJECT, configuration.authSubject)
        assertEquals(BuildConfig.CONTACTS_BFF_AUTH_ROLES, configuration.authRoles)
        assertEquals(BuildConfig.CONTACTS_BFF_API_PREFIX, configuration.apiPrefix)
    }
}
