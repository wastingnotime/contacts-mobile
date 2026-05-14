package org.wastingnotime.contactsmobile.interfaces

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Density
import org.wastingnotime.contactsmobile.domain.Contact
import org.wastingnotime.contactsmobile.interfaces.theme.ContactsTheme
import org.wastingnotime.contactsmobile.interfaces.ui.CreateContactFormState
import org.wastingnotime.contactsmobile.interfaces.ui.CreateContactUiState
import org.wastingnotime.contactsmobile.interfaces.ui.ContactDetailUiState
import org.wastingnotime.contactsmobile.interfaces.ui.EditContactUiState
import org.wastingnotime.contactsmobile.interfaces.ui.ContactsScreen
import org.wastingnotime.contactsmobile.interfaces.ui.ContactsUiState
import org.wastingnotime.contactsmobile.interfaces.ui.ContactsRoute
import org.wastingnotime.contactsmobile.interfaces.ui.ContactsViewModel

@Composable
fun ContactsApp(viewModel: ContactsViewModel) {
    val darkTheme = isSystemInDarkTheme()
    ContactsAppContent(
        uiState = null,
        detailUiState = null,
        createUiState = null,
        editUiState = null,
        onContactClick = {},
        onBack = {},
        onRefresh = {},
        onRetry = {},
        onCreateContact = {},
        onCloseCreateContact = {},
        onCreateFirstNameChange = {},
        onCreateLastNameChange = {},
        onCreatePhoneNumberChange = {},
        onCreateSubmit = {},
        onCreateOpenContact = {},
        onEditContact = {},
        onCloseEditContact = {},
        onEditFirstNameChange = {},
        onEditLastNameChange = {},
        onEditPhoneNumberChange = {},
        onEditSubmit = {},
        onEditOpenContact = {},
        darkTheme = darkTheme,
        dynamicColor = true,
        viewModel = viewModel,
    )
}

@Composable
internal fun ContactsAppContent(
    uiState: ContactsUiState?,
    detailUiState: ContactDetailUiState?,
    createUiState: CreateContactUiState?,
    editUiState: EditContactUiState? = null,
    onContactClick: (Contact) -> Unit,
    onBack: () -> Unit,
    onRefresh: () -> Unit,
    onRetry: () -> Unit,
    onCreateContact: () -> Unit,
    onCloseCreateContact: () -> Unit,
    onCreateFirstNameChange: (String) -> Unit,
    onCreateLastNameChange: (String) -> Unit,
    onCreatePhoneNumberChange: (String) -> Unit,
    onCreateSubmit: () -> Unit,
    onCreateOpenContact: () -> Unit,
    onEditContact: () -> Unit,
    onCloseEditContact: () -> Unit,
    onEditFirstNameChange: (String) -> Unit,
    onEditLastNameChange: (String) -> Unit,
    onEditPhoneNumberChange: (String) -> Unit,
    onEditSubmit: () -> Unit,
    onEditOpenContact: () -> Unit,
    darkTheme: Boolean = false,
    dynamicColor: Boolean = true,
    viewModel: ContactsViewModel? = null,
) {
    ContactsTheme(darkTheme = darkTheme, dynamicColor = dynamicColor) {
        if (viewModel != null) {
            ContactsRoute(viewModel = viewModel)
        } else {
            ContactsScreen(
                uiState = uiState ?: ContactsUiState.Loading,
                detailUiState = detailUiState ?: ContactDetailUiState.Hidden,
                createUiState = createUiState ?: CreateContactUiState.Hidden,
                editUiState = editUiState ?: EditContactUiState.Hidden,
                onContactClick = onContactClick,
                onBack = onBack,
                onRefresh = onRefresh,
                onRetry = onRetry,
                onCreateContact = onCreateContact,
                onCloseCreateContact = onCloseCreateContact,
                onCreateFirstNameChange = onCreateFirstNameChange,
                onCreateLastNameChange = onCreateLastNameChange,
                onCreatePhoneNumberChange = onCreatePhoneNumberChange,
                onCreateSubmit = onCreateSubmit,
                onCreateOpenContact = onCreateOpenContact,
                onEditContact = onEditContact,
                onCloseEditContact = onCloseEditContact,
                onEditFirstNameChange = onEditFirstNameChange,
                onEditLastNameChange = onEditLastNameChange,
                onEditPhoneNumberChange = onEditPhoneNumberChange,
                onEditSubmit = onEditSubmit,
                onEditOpenContact = onEditOpenContact,
            )
        }
    }
}

@Composable
private fun ContactsAppPreview(
    uiState: ContactsUiState,
    detailUiState: ContactDetailUiState,
    createUiState: CreateContactUiState = CreateContactUiState.Hidden,
    editUiState: EditContactUiState = EditContactUiState.Hidden,
    darkTheme: Boolean,
) {
    ContactsAppContent(
        uiState = uiState,
        detailUiState = detailUiState,
        createUiState = createUiState,
        editUiState = editUiState,
        onContactClick = {},
        onBack = {},
        onRefresh = {},
        onRetry = {},
        onCreateContact = {},
        onCloseCreateContact = {},
        onCreateFirstNameChange = {},
        onCreateLastNameChange = {},
        onCreatePhoneNumberChange = {},
        onCreateSubmit = {},
        onCreateOpenContact = {},
        onEditContact = {},
        onCloseEditContact = {},
        onEditFirstNameChange = {},
        onEditLastNameChange = {},
        onEditPhoneNumberChange = {},
        onEditSubmit = {},
        onEditOpenContact = {},
        darkTheme = darkTheme,
        dynamicColor = false,
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
        createUiState = CreateContactUiState.Hidden,
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

@Preview(showBackground = true, name = "Create Light", group = "Create")
@Composable
private fun ContactsAppCreateLightPreview() {
    ContactsAppPreview(
        uiState = ContactsUiState.Loaded(
            contacts = previewContacts(),
        ),
        detailUiState = ContactDetailUiState.Hidden,
        createUiState = CreateContactUiState.Form(
            CreateContactFormState(
                firstName = "Ada",
                lastName = "Lovelace",
                phoneNumber = "555-0100",
            ),
        ),
        darkTheme = false,
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

@Preview(showBackground = true, name = "Loaded Font Light", group = "Font Loaded")
@Composable
private fun ContactsAppLoadedFontLightPreview() {
    ContactsAppFontScalePreview(
        fontScale = 1.0f,
        uiState = ContactsUiState.Loaded(
            contacts = previewContacts(),
        ),
        detailUiState = ContactDetailUiState.Hidden,
        darkTheme = false,
    )
}

@Preview(showBackground = true, name = "Loaded Font Large Light", group = "Font Loaded")
@Composable
private fun ContactsAppLoadedFontLargeLightPreview() {
    ContactsAppFontScalePreview(
        fontScale = 1.3f,
        uiState = ContactsUiState.Loaded(
            contacts = previewContacts(),
        ),
        detailUiState = ContactDetailUiState.Hidden,
        darkTheme = false,
    )
}

@Preview(showBackground = true, name = "Loaded Font Dark", group = "Font Loaded")
@Composable
private fun ContactsAppLoadedFontDarkPreview() {
    ContactsAppFontScalePreview(
        fontScale = 1.0f,
        uiState = ContactsUiState.Loaded(
            contacts = previewContacts(),
        ),
        detailUiState = ContactDetailUiState.Hidden,
        darkTheme = true,
    )
}

@Preview(showBackground = true, name = "Loaded Font Large Dark", group = "Font Loaded")
@Composable
private fun ContactsAppLoadedFontLargeDarkPreview() {
    ContactsAppFontScalePreview(
        fontScale = 1.3f,
        uiState = ContactsUiState.Loaded(
            contacts = previewContacts(),
        ),
        detailUiState = ContactDetailUiState.Hidden,
        darkTheme = true,
    )
}

@Preview(showBackground = true, name = "Detail Font Light", group = "Font Detail")
@Composable
private fun ContactsAppDetailFontLightPreview() {
    val contacts = previewContacts()
    ContactsAppFontScalePreview(
        fontScale = 1.0f,
        uiState = ContactsUiState.Loaded(
            contacts = contacts,
        ),
        detailUiState = ContactDetailUiState.Loaded(
            contact = contacts.first(),
        ),
        darkTheme = false,
    )
}

@Preview(showBackground = true, name = "Detail Font Large Light", group = "Font Detail")
@Composable
private fun ContactsAppDetailFontLargeLightPreview() {
    val contacts = previewContacts()
    ContactsAppFontScalePreview(
        fontScale = 1.3f,
        uiState = ContactsUiState.Loaded(
            contacts = contacts,
        ),
        detailUiState = ContactDetailUiState.Loaded(
            contact = contacts.first(),
        ),
        darkTheme = false,
    )
}

@Preview(showBackground = true, name = "Detail Font Dark", group = "Font Detail")
@Composable
private fun ContactsAppDetailFontDarkPreview() {
    val contacts = previewContacts()
    ContactsAppFontScalePreview(
        fontScale = 1.0f,
        uiState = ContactsUiState.Loaded(
            contacts = contacts,
        ),
        detailUiState = ContactDetailUiState.Loaded(
            contact = contacts.first(),
        ),
        darkTheme = true,
    )
}

@Preview(showBackground = true, name = "Detail Font Large Dark", group = "Font Detail")
@Composable
private fun ContactsAppDetailFontLargeDarkPreview() {
    val contacts = previewContacts()
    ContactsAppFontScalePreview(
        fontScale = 1.3f,
        uiState = ContactsUiState.Loaded(
            contacts = contacts,
        ),
        detailUiState = ContactDetailUiState.Loaded(
            contact = contacts.first(),
        ),
        darkTheme = true,
    )
}

@Composable
private fun ContactsAppFontScalePreview(
    fontScale: Float,
    uiState: ContactsUiState,
    detailUiState: ContactDetailUiState,
    darkTheme: Boolean,
) {
    val density = LocalDensity.current
    CompositionLocalProvider(
        LocalDensity provides Density(
            density = density.density,
            fontScale = fontScale,
        ),
    ) {
        ContactsAppPreview(
            uiState = uiState,
            detailUiState = detailUiState,
            darkTheme = darkTheme,
        )
    }
}

@Preview(showBackground = true, name = "Text Loaded Light", group = "Text Loaded")
@Composable
private fun ContactsAppTextLoadedLightPreview() {
    ContactsAppPreview(
        uiState = ContactsUiState.Loaded(
            contacts = previewStressContacts(),
        ),
        detailUiState = ContactDetailUiState.Hidden,
        darkTheme = false,
    )
}

@Preview(showBackground = true, name = "Text Loaded Dark", group = "Text Loaded")
@Composable
private fun ContactsAppTextLoadedDarkPreview() {
    ContactsAppPreview(
        uiState = ContactsUiState.Loaded(
            contacts = previewStressContacts(),
        ),
        detailUiState = ContactDetailUiState.Hidden,
        darkTheme = true,
    )
}

@Preview(showBackground = true, name = "Text Detail Light", group = "Text Detail")
@Composable
private fun ContactsAppTextDetailLightPreview() {
    val contacts = previewStressContacts()
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

@Preview(showBackground = true, name = "Text Detail Dark", group = "Text Detail")
@Composable
private fun ContactsAppTextDetailDarkPreview() {
    val contacts = previewStressContacts()
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

private fun previewStressContacts(): List<Contact> = listOf(
    Contact(
        id = "contact-101",
        firstName = "Maximilian-Josephine",
        lastName = "Montgomery-Wellington-Smythe",
        phoneNumber = "+1 (555) 010-2024 extension 12345",
    ),
    Contact(
        id = "contact-102",
        firstName = "Alexandria-Catherine",
        lastName = "Fitzgerald-Vanderbilt",
        phoneNumber = "+1 (555) 010-2025 extension 67890",
    ),
)
