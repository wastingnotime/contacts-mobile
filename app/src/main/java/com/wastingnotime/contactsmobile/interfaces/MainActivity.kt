package com.wastingnotime.contactsmobile.interfaces

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.lifecycle.viewmodel.compose.viewModel
import com.wastingnotime.contactsmobile.BuildConfig
import com.wastingnotime.contactsmobile.application.LoadContactById
import com.wastingnotime.contactsmobile.application.LoadContacts
import com.wastingnotime.contactsmobile.infrastructure.config.ContactsApiBaseUrlConfiguration
import com.wastingnotime.contactsmobile.infrastructure.config.ContactsApiBaseUrlResolver
import com.wastingnotime.contactsmobile.infrastructure.http.DefaultContactsRepository
import com.wastingnotime.contactsmobile.infrastructure.http.HttpContactsApiClient
import com.wastingnotime.contactsmobile.interfaces.ui.ContactsRoute
import com.wastingnotime.contactsmobile.interfaces.ui.ContactsViewModel
import com.wastingnotime.contactsmobile.interfaces.ui.ContactsViewModelFactory

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
        val apiClient = HttpContactsApiClient(baseUrl)
        val repository = DefaultContactsRepository(apiClient)
        val loadContacts = LoadContacts(repository)
        val loadContactById = LoadContactById(repository)
        val factory = ContactsViewModelFactory(loadContacts, loadContactById)

        setContent {
            MaterialTheme {
                val viewModel: ContactsViewModel = viewModel(factory = factory)
                ContactsRoute(viewModel = viewModel)
            }
        }
    }
}
