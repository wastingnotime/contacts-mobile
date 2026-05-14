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
    ContactsAppContent(
        uiState = null,
        detailUiState = null,
        onContactClick = {},
        onBack = {},
        onRefresh = {},
        onRetry = {},
        viewModel = viewModel,
    )
}

@Composable
internal fun ContactsAppContent(
    uiState: ContactsUiState?,
    detailUiState: ContactDetailUiState?,
    onContactClick: (Contact) -> Unit,
    onBack: () -> Unit,
    onRefresh: () -> Unit,
    onRetry: () -> Unit,
    viewModel: ContactsViewModel? = null,
) {
    MaterialTheme {
        if (viewModel != null) {
            ContactsRoute(viewModel = viewModel)
        } else {
            ContactsScreen(
                uiState = uiState ?: ContactsUiState.Loading,
                detailUiState = detailUiState ?: ContactDetailUiState.Hidden,
                onContactClick = onContactClick,
                onBack = onBack,
                onRefresh = onRefresh,
                onRetry = onRetry,
            )
        }
    }
}

@Preview(showBackground = true, name = "ContactsApp - Loading")
@Composable
private fun ContactsAppLoadingPreview() {
    ContactsAppPreview(
        uiState = ContactsUiState.Loading,
        detailUiState = ContactDetailUiState.Hidden,
    )
}

@Preview(showBackground = true, name = "ContactsApp - Empty")
@Composable
private fun ContactsAppEmptyPreview() {
    ContactsAppPreview(
        uiState = ContactsUiState.Empty(),
        detailUiState = ContactDetailUiState.Hidden,
    )
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

@Preview(showBackground = true, name = "ContactsApp - Loaded With Refresh Banner")
@Composable
private fun ContactsAppLoadedWithTransientErrorPreview() {
    ContactsAppPreview(
        uiState = ContactsUiState.Loaded(
            contacts = previewContacts(),
            transientErrorMessage = "Unable to refresh contacts right now.",
        ),
        detailUiState = ContactDetailUiState.Hidden,
    )
}

@Preview(showBackground = true, name = "ContactsApp - Detail")
@Composable
private fun ContactsAppDetailPreview() {
    val contacts = previewContacts()
    ContactsAppPreview(
        uiState = ContactsUiState.Loaded(
            contacts = contacts,
        ),
        detailUiState = ContactDetailUiState.Loaded(
            contact = contacts.first(),
        ),
    )
}

@Preview(showBackground = true, name = "ContactsApp - Detail With Refresh Banner")
@Composable
private fun ContactsAppDetailWithTransientErrorPreview() {
    val contacts = previewContacts()
    ContactsAppPreview(
        uiState = ContactsUiState.Loaded(
            contacts = contacts,
        ),
        detailUiState = ContactDetailUiState.Loaded(
            contact = contacts.first(),
            transientErrorMessage = "Detail refreshed with a warning.",
        ),
    )
}

@Composable
private fun ContactsAppPreview(
    uiState: ContactsUiState,
    detailUiState: ContactDetailUiState,
) {
    ContactsAppContent(
        uiState = uiState,
        detailUiState = detailUiState,
        onContactClick = {},
        onBack = {},
        onRefresh = {},
        onRetry = {},
    )
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
