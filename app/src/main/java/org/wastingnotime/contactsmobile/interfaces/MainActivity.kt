package org.wastingnotime.contactsmobile.interfaces

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.viewmodel.compose.viewModel
import org.wastingnotime.contactsmobile.BuildConfig
import org.wastingnotime.contactsmobile.application.LoadContactById
import org.wastingnotime.contactsmobile.application.LoadContacts
import org.wastingnotime.contactsmobile.infrastructure.config.ContactsApiAuthConfiguration
import org.wastingnotime.contactsmobile.infrastructure.config.ContactsApiAuthHeadersResolver
import org.wastingnotime.contactsmobile.infrastructure.config.ContactsApiBaseUrlConfiguration
import org.wastingnotime.contactsmobile.infrastructure.config.ContactsApiBaseUrlResolver
import org.wastingnotime.contactsmobile.infrastructure.http.DefaultContactsRepository
import org.wastingnotime.contactsmobile.infrastructure.http.HttpContactsApiClient
import org.wastingnotime.contactsmobile.interfaces.ui.ContactsViewModel
import org.wastingnotime.contactsmobile.interfaces.ui.ContactsViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val baseUrl = ContactsApiBaseUrlResolver.resolve(
            ContactsApiBaseUrlConfiguration(
                environment = BuildConfig.CONTACTS_API_ENVIRONMENT,
                emulatorBaseUrl = BuildConfig.CONTACTS_API_EMULATOR_BASE_URL,
                localDeviceBaseUrl = BuildConfig.CONTACTS_API_LOCAL_DEVICE_BASE_URL,
                productionBaseUrl = BuildConfig.CONTACTS_API_PRODUCTION_BASE_URL,
            ),
        )
        val authHeaders = ContactsApiAuthHeadersResolver.resolve(
            ContactsApiAuthConfiguration(
                subject = BuildConfig.CONTACTS_API_AUTH_SUBJECT,
                roles = BuildConfig.CONTACTS_API_AUTH_ROLES,
            ),
        )
        val apiClient = HttpContactsApiClient(
            baseUrl = baseUrl,
            authHeaders = authHeaders,
        )
        val repository = DefaultContactsRepository(apiClient)
        val loadContacts = LoadContacts(repository)
        val loadContactById = LoadContactById(repository)
        val factory = ContactsViewModelFactory(loadContacts, loadContactById)

        setContent {
            val viewModel: ContactsViewModel = viewModel(factory = factory)
            ContactsApp(viewModel = viewModel)
        }
    }
}
