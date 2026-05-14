package org.wastingnotime.contactsmobile.interfaces

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import org.wastingnotime.contactsmobile.domain.Contact
import org.wastingnotime.contactsmobile.interfaces.ui.ContactDetailUiState
import org.wastingnotime.contactsmobile.interfaces.ui.ContactsScreen
import org.wastingnotime.contactsmobile.interfaces.ui.ContactsUiState
import org.wastingnotime.contactsmobile.interfaces.ui.ContactsRoute
import org.wastingnotime.contactsmobile.interfaces.ui.ContactsViewModel

@Composable
fun ContactsApp(viewModel: ContactsViewModel) {
    MaterialTheme {
        ContactsRoute(viewModel = viewModel)
    }
}

@Preview(showBackground = true, name = "ContactsApp - Loaded")
@Composable
private fun ContactsAppLoadedPreview() {
    ContactsAppPreview(
        uiState = ContactsUiState.Loaded(
            contacts = previewContacts(),
        ),
        detailUiState = ContactDetailUiState.Hidden,
    )
}

@Preview(showBackground = true, name = "ContactsApp - Detail")
@Composable
private fun ContactsAppDetailPreview() {
    ContactsAppPreview(
        uiState = ContactsUiState.Loaded(
            contacts = previewContacts(),
        ),
        detailUiState = ContactDetailUiState.Loaded(
            contact = previewContacts().first(),
            transientErrorMessage = "Detail refreshed with a warning.",
        ),
    )
}

@Composable
private fun ContactsAppPreview(
    uiState: ContactsUiState,
    detailUiState: ContactDetailUiState,
) {
    MaterialTheme {
        ContactsScreen(
            uiState = uiState,
            detailUiState = detailUiState,
            onContactClick = {},
            onBack = {},
            onRefresh = {},
            onRetry = {},
        )
    }
}

private fun previewContacts(): List<Contact> = listOf(
    Contact(
        id = "contact-1",
        firstName = "Ada",
        lastName = "Lovelace",
        phoneNumber = "555-0100",
    ),
    Contact(
        id = "contact-2",
        firstName = "Grace",
        lastName = "Hopper",
        phoneNumber = "555-0101",
    ),
)
