package org.wastingnotime.contactsmobile.interfaces

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
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
        darkTheme = false,
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
    darkTheme: Boolean = false,
    viewModel: ContactsViewModel? = null,
) {
    ContactsAppTheme(darkTheme = darkTheme) {
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

@Composable
private fun ContactsAppPreview(
    uiState: ContactsUiState,
    detailUiState: ContactDetailUiState,
    darkTheme: Boolean,
) {
    ContactsAppContent(
        uiState = uiState,
        detailUiState = detailUiState,
        onContactClick = {},
        onBack = {},
        onRefresh = {},
        onRetry = {},
        darkTheme = darkTheme,
    )
}

@Composable
private fun ContactsAppTheme(
    darkTheme: Boolean = false,
    content: @Composable () -> Unit,
) {
    MaterialTheme(
        colorScheme = if (darkTheme) darkColorScheme() else lightColorScheme(),
        content = content,
    )
}

@Preview(showBackground = true, name = "Loading Light", group = "Loading")
@Composable
private fun ContactsAppLoadingLightPreview() {
    ContactsAppPreview(
        uiState = ContactsUiState.Loading,
        detailUiState = ContactDetailUiState.Hidden,
        darkTheme = false,
    )
}

@Preview(showBackground = true, name = "Loading Dark", group = "Loading")
@Composable
private fun ContactsAppLoadingDarkPreview() {
    ContactsAppPreview(
        uiState = ContactsUiState.Loading,
        detailUiState = ContactDetailUiState.Hidden,
        darkTheme = true,
    )
}

@Preview(showBackground = true, name = "Empty Light", group = "Empty")
@Composable
private fun ContactsAppEmptyLightPreview() {
    ContactsAppPreview(
        uiState = ContactsUiState.Empty(),
        detailUiState = ContactDetailUiState.Hidden,
        darkTheme = false,
    )
}

@Preview(showBackground = true, name = "Empty Dark", group = "Empty")
@Composable
private fun ContactsAppEmptyDarkPreview() {
    ContactsAppPreview(
        uiState = ContactsUiState.Empty(),
        detailUiState = ContactDetailUiState.Hidden,
        darkTheme = true,
    )
}

@Preview(showBackground = true, name = "Loaded Light", group = "Loaded")
@Composable
private fun ContactsAppLoadedLightPreview() {
    ContactsAppPreview(
        uiState = ContactsUiState.Loaded(
            contacts = previewContacts(),
        ),
        detailUiState = ContactDetailUiState.Hidden,
        darkTheme = false,
    )
}

@Preview(showBackground = true, name = "Loaded Dark", group = "Loaded")
@Composable
private fun ContactsAppLoadedDarkPreview() {
    ContactsAppPreview(
        uiState = ContactsUiState.Loaded(
            contacts = previewContacts(),
        ),
        detailUiState = ContactDetailUiState.Hidden,
        darkTheme = true,
    )
}

@Preview(showBackground = true, name = "Loaded Banner Light", group = "Loaded Banner")
@Composable
private fun ContactsAppLoadedWithTransientErrorLightPreview() {
    ContactsAppPreview(
        uiState = ContactsUiState.Loaded(
            contacts = previewContacts(),
            transientErrorMessage = "Unable to refresh contacts right now.",
        ),
        detailUiState = ContactDetailUiState.Hidden,
        darkTheme = false,
    )
}

@Preview(showBackground = true, name = "Loaded Banner Dark", group = "Loaded Banner")
@Composable
private fun ContactsAppLoadedWithTransientErrorDarkPreview() {
    ContactsAppPreview(
        uiState = ContactsUiState.Loaded(
            contacts = previewContacts(),
            transientErrorMessage = "Unable to refresh contacts right now.",
        ),
        detailUiState = ContactDetailUiState.Hidden,
        darkTheme = true,
    )
}

@Preview(showBackground = true, name = "Detail Light", group = "Detail")
@Composable
private fun ContactsAppDetailLightPreview() {
    val contacts = previewContacts()
    ContactsAppPreview(
        uiState = ContactsUiState.Loaded(
            contacts = contacts,
        ),
        detailUiState = ContactDetailUiState.Loaded(
            contact = contacts.first(),
        ),
        darkTheme = false,
    )
}

@Preview(showBackground = true, name = "Detail Dark", group = "Detail")
@Composable
private fun ContactsAppDetailDarkPreview() {
    val contacts = previewContacts()
    ContactsAppPreview(
        uiState = ContactsUiState.Loaded(
            contacts = contacts,
        ),
        detailUiState = ContactDetailUiState.Loaded(
            contact = contacts.first(),
        ),
        darkTheme = true,
    )
}

@Preview(showBackground = true, name = "Detail Banner Light", group = "Detail Banner")
@Composable
private fun ContactsAppDetailWithTransientErrorLightPreview() {
    val contacts = previewContacts()
    ContactsAppPreview(
        uiState = ContactsUiState.Loaded(
            contacts = contacts,
        ),
        detailUiState = ContactDetailUiState.Loaded(
            contact = contacts.first(),
            transientErrorMessage = "Detail refreshed with a warning.",
        ),
        darkTheme = false,
    )
}

@Preview(showBackground = true, name = "Detail Banner Dark", group = "Detail Banner")
@Composable
private fun ContactsAppDetailWithTransientErrorDarkPreview() {
    val contacts = previewContacts()
    ContactsAppPreview(
        uiState = ContactsUiState.Loaded(
            contacts = contacts,
        ),
        detailUiState = ContactDetailUiState.Loaded(
            contact = contacts.first(),
            transientErrorMessage = "Detail refreshed with a warning.",
        ),
        darkTheme = true,
    )
}

@Preview(
    showBackground = true,
    widthDp = 360,
    name = "Loaded Compact Light",
    group = "Width Loaded",
)
@Composable
private fun ContactsAppLoadedCompactLightPreview() {
    ContactsAppPreview(
        uiState = ContactsUiState.Loaded(
            contacts = previewContacts(),
        ),
        detailUiState = ContactDetailUiState.Hidden,
        darkTheme = false,
    )
}

@Preview(
    showBackground = true,
    widthDp = 840,
    name = "Loaded Expanded Light",
    group = "Width Loaded",
)
@Composable
private fun ContactsAppLoadedExpandedLightPreview() {
    ContactsAppPreview(
        uiState = ContactsUiState.Loaded(
            contacts = previewContacts(),
        ),
        detailUiState = ContactDetailUiState.Hidden,
        darkTheme = false,
    )
}

@Preview(
    showBackground = true,
    widthDp = 360,
    name = "Loaded Compact Dark",
    group = "Width Loaded",
)
@Composable
private fun ContactsAppLoadedCompactDarkPreview() {
    ContactsAppPreview(
        uiState = ContactsUiState.Loaded(
            contacts = previewContacts(),
        ),
        detailUiState = ContactDetailUiState.Hidden,
        darkTheme = true,
    )
}

@Preview(
    showBackground = true,
    widthDp = 840,
    name = "Loaded Expanded Dark",
    group = "Width Loaded",
)
@Composable
private fun ContactsAppLoadedExpandedDarkPreview() {
    ContactsAppPreview(
        uiState = ContactsUiState.Loaded(
            contacts = previewContacts(),
        ),
        detailUiState = ContactDetailUiState.Hidden,
        darkTheme = true,
    )
}

@Preview(
    showBackground = true,
    widthDp = 360,
    name = "Detail Compact Light",
    group = "Width Detail",
)
@Composable
private fun ContactsAppDetailCompactLightPreview() {
    val contacts = previewContacts()
    ContactsAppPreview(
        uiState = ContactsUiState.Loaded(
            contacts = contacts,
        ),
        detailUiState = ContactDetailUiState.Loaded(
            contact = contacts.first(),
        ),
        darkTheme = false,
    )
}

@Preview(
    showBackground = true,
    widthDp = 840,
    name = "Detail Expanded Light",
    group = "Width Detail",
)
@Composable
private fun ContactsAppDetailExpandedLightPreview() {
    val contacts = previewContacts()
    ContactsAppPreview(
        uiState = ContactsUiState.Loaded(
            contacts = contacts,
        ),
        detailUiState = ContactDetailUiState.Loaded(
            contact = contacts.first(),
        ),
        darkTheme = false,
    )
}

@Preview(
    showBackground = true,
    widthDp = 360,
    name = "Detail Compact Dark",
    group = "Width Detail",
)
@Composable
private fun ContactsAppDetailCompactDarkPreview() {
    val contacts = previewContacts()
    ContactsAppPreview(
        uiState = ContactsUiState.Loaded(
            contacts = contacts,
        ),
        detailUiState = ContactDetailUiState.Loaded(
            contact = contacts.first(),
        ),
        darkTheme = true,
    )
}

@Preview(
    showBackground = true,
    widthDp = 840,
    name = "Detail Expanded Dark",
    group = "Width Detail",
)
@Composable
private fun ContactsAppDetailExpandedDarkPreview() {
    val contacts = previewContacts()
    ContactsAppPreview(
        uiState = ContactsUiState.Loaded(
            contacts = contacts,
        ),
        detailUiState = ContactDetailUiState.Loaded(
            contact = contacts.first(),
        ),
        darkTheme = true,
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
