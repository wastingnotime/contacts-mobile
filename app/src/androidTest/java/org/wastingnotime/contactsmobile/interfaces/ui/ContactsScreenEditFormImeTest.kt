package org.wastingnotime.contactsmobile.interfaces.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.test.assertIsFocused
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performImeAction
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.unit.dp
import java.util.concurrent.atomic.AtomicBoolean
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.wastingnotime.contactsmobile.domain.Contact

class ContactsScreenEditFormImeTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun advancesThroughTheEditFormFieldsAndSubmitsFromPhoneNumber() {
        val submitted = AtomicBoolean(false)
        val contact = Contact(
            id = "contact-4",
            firstName = "Grace",
            lastName = "Hopper",
            phoneNumber = "5550111",
        )

        composeTestRule.setContent {
            TestSurface {
                ContactsScreen(
                    uiState = ContactsUiState.Loaded(contacts = listOf(contact)),
                    detailUiState = ContactDetailUiState.Hidden,
                    createUiState = CreateContactUiState.Hidden,
                    editUiState = EditContactUiState.Form(
                        EditContactFormState(
                            contactId = contact.id,
                            firstName = contact.firstName,
                            lastName = contact.lastName,
                            phoneNumber = contact.phoneNumber,
                        ),
                    ),
                    modifier = Modifier.fillMaxSize(),
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
                    onEditSubmit = { submitted.set(true) },
                    onEditOpenContact = {},
                )
            }
        }

        composeTestRule.onNodeWithTag("edit-first-name").performTextInput("Ada")
        composeTestRule.onNodeWithTag("edit-first-name").performImeAction()
        composeTestRule.onNodeWithTag("edit-last-name").assertIsFocused()

        composeTestRule.onNodeWithTag("edit-last-name").performTextInput("Byron")
        composeTestRule.onNodeWithTag("edit-last-name").performImeAction()
        composeTestRule.onNodeWithTag("edit-phone-number").assertIsFocused()

        composeTestRule.onNodeWithTag("edit-phone-number").performTextInput("5550199")
        composeTestRule.onNodeWithTag("edit-phone-number").performImeAction()

        composeTestRule.runOnIdle {
            assertTrue("Expected the phone-number field IME action to submit the edit form.", submitted.get())
        }
    }
}

@Composable
private fun TestSurface(content: @Composable () -> Unit) {
    Box(
        modifier = Modifier
            .size(1080.dp, 1920.dp)
            .testTag("edit-form-surface"),
    ) {
        content()
    }
}
