package org.wastingnotime.contactsmobile.interfaces

import org.wastingnotime.contactsmobile.infrastructure.config.ContactsBffAuthConfiguration
import org.wastingnotime.contactsmobile.infrastructure.config.ContactsBffAuthHeadersResolver
import org.wastingnotime.contactsmobile.infrastructure.config.ContactsBffBaseUrlConfiguration
import org.wastingnotime.contactsmobile.infrastructure.config.ContactsBffBaseUrlResolver
import org.wastingnotime.contactsmobile.infrastructure.http.ContactsBffApiSurfaceConfiguration
import org.wastingnotime.contactsmobile.infrastructure.http.ContactsBffApiSurfaceResolver
import org.wastingnotime.contactsmobile.infrastructure.http.DefaultContactsRepository
import org.wastingnotime.contactsmobile.infrastructure.http.HttpContactsBffClient
import org.wastingnotime.contactsmobile.interfaces.ui.ContactsViewModelFactory

data class ContactsBffBootstrapConfiguration(
    val environment: String,
    val emulatorBaseUrl: String,
    val localDeviceBaseUrl: String,
    val productionBaseUrl: String,
    val authSubject: String,
    val authRoles: String,
    val apiPrefix: String,
)

data class ContactsBffBootstrap(
    val viewModelFactory: ContactsViewModelFactory,
)

data class ContactsBffBootstrapDependencies(
    val apiClient: HttpContactsBffClient,
    val repository: DefaultContactsRepository,
)

object ContactsBffBootstrapDependenciesResolver {
    fun resolve(configuration: ContactsBffBootstrapConfiguration): ContactsBffBootstrapDependencies {
        val baseUrl = ContactsBffBaseUrlResolver.resolve(
            ContactsBffBaseUrlConfiguration(
                environment = configuration.environment,
                emulatorBaseUrl = configuration.emulatorBaseUrl,
                localDeviceBaseUrl = configuration.localDeviceBaseUrl,
                productionBaseUrl = configuration.productionBaseUrl,
            ),
        )
        val authHeaders = ContactsBffAuthHeadersResolver.resolve(
            ContactsBffAuthConfiguration(
                subject = configuration.authSubject,
                roles = configuration.authRoles,
            ),
        )
        val apiSurface = ContactsBffApiSurfaceResolver.resolve(
            ContactsBffApiSurfaceConfiguration(
                apiPrefix = configuration.apiPrefix,
            ),
        )
        val apiClient = HttpContactsBffClient(
            baseUrl = baseUrl,
            apiSurface = apiSurface,
            authHeaders = authHeaders,
        )
        val repository = DefaultContactsRepository(apiClient)
        return ContactsBffBootstrapDependencies(
            apiClient = apiClient,
            repository = repository,
        )
    }
}

object ContactsBffBootstrapper {
    fun build(configuration: ContactsBffBootstrapConfiguration): ContactsBffBootstrap {
        val dependencies = ContactsBffBootstrapDependenciesResolver.resolve(configuration)
        val useCases = ContactsBffUseCaseAssembly.assemble(dependencies.repository)
        val factory = ContactsViewModelFactory(
            useCases.loadContacts,
            useCases.loadContactById,
            useCases.createContact,
            useCases.updateContact,
            useCases.deleteContact,
        )
        return ContactsBffBootstrap(viewModelFactory = factory)
    }

    fun build(): ContactsBffBootstrap {
        return build(
            ContactsBffBootstrapConfigurationResolver.resolve(
                ContactsBffBootstrapBuildConfigurationSource.resolve(),
            ),
        )
    }
}
