package org.wastingnotime.contactsmobile.interfaces

import org.wastingnotime.contactsmobile.BuildConfig

object ContactsBffBootstrapBuildConfigurationSource {
    fun resolve(): ContactsBffBootstrapBuildConfiguration {
        return ContactsBffBootstrapBuildConfiguration(
            environment = BuildConfig.CONTACTS_BFF_ENVIRONMENT,
            emulatorBaseUrl = BuildConfig.CONTACTS_BFF_EMULATOR_BASE_URL,
            localDeviceBaseUrl = BuildConfig.CONTACTS_BFF_LOCAL_DEVICE_BASE_URL,
            productionBaseUrl = BuildConfig.CONTACTS_BFF_PRODUCTION_BASE_URL,
            authSubject = BuildConfig.CONTACTS_BFF_AUTH_SUBJECT,
            authRoles = BuildConfig.CONTACTS_BFF_AUTH_ROLES,
            apiPrefix = BuildConfig.CONTACTS_BFF_API_PREFIX,
        )
    }
}
