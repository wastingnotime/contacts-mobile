package org.wastingnotime.contactsmobile.interfaces

data class ContactsBffBootstrapBuildConfiguration(
    val environment: String,
    val emulatorBaseUrl: String,
    val localDeviceBaseUrl: String,
    val productionBaseUrl: String,
    val authSubject: String,
    val authRoles: String,
    val apiPrefix: String,
)

object ContactsBffBootstrapConfigurationResolver {
    fun resolve(configuration: ContactsBffBootstrapBuildConfiguration): ContactsBffBootstrapConfiguration {
        val environment = configuration.environment.trim().lowercase()
        require(environment.isNotBlank()) {
            "contactsBffEnvironment must not be blank."
        }
        require(environment in setOf("emulator", "local_device", "local-device", "device", "production")) {
            "contactsBffEnvironment must be emulator, local_device, or production."
        }

        val emulatorBaseUrl = configuration.emulatorBaseUrl.trim()
        require(emulatorBaseUrl.isNotBlank()) {
            "contactsBffEmulatorBaseUrl must not be blank."
        }

        val localDeviceBaseUrl = configuration.localDeviceBaseUrl.trim()
        require(localDeviceBaseUrl.isNotBlank()) {
            "contactsBffLocalDeviceBaseUrl must not be blank."
        }

        val productionBaseUrl = configuration.productionBaseUrl.trim()
        if (environment == "production") {
            require(productionBaseUrl.isNotBlank()) {
                "contactsBffProductionBaseUrl must be configured when contactsBffEnvironment=production."
            }
        }

        val authSubject = configuration.authSubject.trim()
        require(authSubject.isNotBlank()) {
            "contactsBffAuthSubject must not be blank."
        }

        val authRoles = configuration.authRoles.trim()
        require(authRoles.isNotBlank()) {
            "contactsBffAuthRoles must not be blank."
        }

        val apiPrefix = configuration.apiPrefix.trim()
        require(apiPrefix.isNotBlank()) {
            "contactsBffApiPrefix must not be blank."
        }

        return ContactsBffBootstrapConfiguration(
            environment = environment,
            emulatorBaseUrl = emulatorBaseUrl,
            localDeviceBaseUrl = localDeviceBaseUrl,
            productionBaseUrl = productionBaseUrl,
            authSubject = authSubject,
            authRoles = authRoles,
            apiPrefix = apiPrefix,
        )
    }
}
