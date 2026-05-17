package org.wastingnotime.contactsmobile.interfaces

import org.wastingnotime.contactsmobile.application.CreateContact
import org.wastingnotime.contactsmobile.application.DeleteContact
import org.wastingnotime.contactsmobile.application.LoadContactById
import org.wastingnotime.contactsmobile.application.LoadContacts
import org.wastingnotime.contactsmobile.application.UpdateContact
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

object ContactsBffBootstrapper {
    fun build(configuration: ContactsBffBootstrapConfiguration): ContactsBffBootstrap {
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
        val loadContacts = LoadContacts(repository)
        val loadContactById = LoadContactById(repository)
        val createContact = CreateContact(repository)
        val updateContact = UpdateContact(repository)
        val deleteContact = DeleteContact(repository)
        val factory = ContactsViewModelFactory(
            loadContacts,
            loadContactById,
            createContact,
            updateContact,
            deleteContact,
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
