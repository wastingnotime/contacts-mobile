package org.wastingnotime.contactsmobile.interfaces

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.viewmodel.compose.viewModel
import org.wastingnotime.contactsmobile.BuildConfig
import org.wastingnotime.contactsmobile.application.LoadContactById
import org.wastingnotime.contactsmobile.application.LoadContacts
import org.wastingnotime.contactsmobile.application.CreateContact
import org.wastingnotime.contactsmobile.application.DeleteContact
import org.wastingnotime.contactsmobile.application.UpdateContact
import org.wastingnotime.contactsmobile.infrastructure.config.ContactsBffAuthConfiguration
import org.wastingnotime.contactsmobile.infrastructure.config.ContactsBffAuthHeadersResolver
import org.wastingnotime.contactsmobile.infrastructure.config.ContactsBffBaseUrlConfiguration
import org.wastingnotime.contactsmobile.infrastructure.config.ContactsBffBaseUrlResolver
import org.wastingnotime.contactsmobile.infrastructure.http.ContactsBffApiSurfaceConfiguration
import org.wastingnotime.contactsmobile.infrastructure.http.ContactsBffApiSurfaceResolver
import org.wastingnotime.contactsmobile.infrastructure.http.DefaultContactsRepository
import org.wastingnotime.contactsmobile.infrastructure.http.HttpContactsBffClient
import org.wastingnotime.contactsmobile.interfaces.ui.ContactsViewModel
import org.wastingnotime.contactsmobile.interfaces.ui.ContactsViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val baseUrl = ContactsBffBaseUrlResolver.resolve(
            ContactsBffBaseUrlConfiguration(
                environment = BuildConfig.CONTACTS_BFF_ENVIRONMENT,
                emulatorBaseUrl = BuildConfig.CONTACTS_BFF_EMULATOR_BASE_URL,
                localDeviceBaseUrl = BuildConfig.CONTACTS_BFF_LOCAL_DEVICE_BASE_URL,
                productionBaseUrl = BuildConfig.CONTACTS_BFF_PRODUCTION_BASE_URL,
            ),
        )
        val authHeaders = ContactsBffAuthHeadersResolver.resolve(
            ContactsBffAuthConfiguration(
                subject = BuildConfig.CONTACTS_BFF_AUTH_SUBJECT,
                roles = BuildConfig.CONTACTS_BFF_AUTH_ROLES,
            ),
        )
        val apiSurface = ContactsBffApiSurfaceResolver.resolve(
            ContactsBffApiSurfaceConfiguration(
                apiPrefix = BuildConfig.CONTACTS_BFF_API_PREFIX,
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
        val factory = ContactsViewModelFactory(loadContacts, loadContactById, createContact, updateContact, deleteContact)

        setContent {
            val viewModel: ContactsViewModel = viewModel(factory = factory)
            ContactsApp(viewModel = viewModel)
        }
    }
}
