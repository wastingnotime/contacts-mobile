package org.wastingnotime.contactsmobile.infrastructure.config

data class ContactsApiBaseUrlConfiguration(
    val environment: String,
    val emulatorBaseUrl: String,
    val localDeviceBaseUrl: String,
    val productionBaseUrl: String,
)

object ContactsApiBaseUrlResolver {
    fun resolve(configuration: ContactsApiBaseUrlConfiguration): String {
        return when (configuration.environment.trim().lowercase()) {
            "emulator" -> configuration.emulatorBaseUrl
            "local_device", "local-device", "device" -> configuration.localDeviceBaseUrl
            "production" -> resolveProductionBaseUrl(configuration.productionBaseUrl)
            else -> error(
                "Unsupported contacts API environment: ${configuration.environment}. " +
                    "Expected emulator, local_device, or production.",
            )
        }
    }

    private fun resolveProductionBaseUrl(baseUrl: String): String {
        val normalized = baseUrl.trim()
        require(normalized.isNotBlank()) {
            "contactsApiProductionBaseUrl must be configured when contactsApiEnvironment=production."
        }
        return normalized
    }
}
