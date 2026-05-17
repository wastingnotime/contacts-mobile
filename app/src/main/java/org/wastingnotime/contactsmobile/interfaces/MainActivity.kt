package org.wastingnotime.contactsmobile.interfaces

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.viewmodel.compose.viewModel
import org.wastingnotime.contactsmobile.interfaces.ui.ContactsViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val factory = ContactsAppStart.start().viewModelFactory

        setContent {
            val viewModel: ContactsViewModel = viewModel(factory = factory)
            ContactsApp(viewModel = viewModel)
        }
    }
}
