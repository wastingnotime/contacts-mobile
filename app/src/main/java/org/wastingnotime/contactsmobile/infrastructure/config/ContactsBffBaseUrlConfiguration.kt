package org.wastingnotime.contactsmobile.infrastructure.config

data class ContactsBffBaseUrlConfiguration(
    val environment: String,
    val emulatorBaseUrl: String,
    val localDeviceBaseUrl: String,
    val productionBaseUrl: String,
)

object ContactsBffBaseUrlResolver {
    fun resolve(configuration: ContactsBffBaseUrlConfiguration): String {
        return when (configuration.environment.trim().lowercase()) {
            "emulator" -> configuration.emulatorBaseUrl
            "local_device", "local-device", "device" -> configuration.localDeviceBaseUrl
            "production" -> resolveProductionBaseUrl(configuration.productionBaseUrl)
            else -> error(
                "Unsupported contacts BFF environment: ${configuration.environment}. " +
                    "Expected emulator, local_device, or production.",
            )
        }
    }

    private fun resolveProductionBaseUrl(baseUrl: String): String {
        val normalized = baseUrl.trim()
        require(normalized.isNotBlank()) {
            "contactsBffProductionBaseUrl must be configured when contactsBffEnvironment=production."
        }
        return normalized
    }
}
