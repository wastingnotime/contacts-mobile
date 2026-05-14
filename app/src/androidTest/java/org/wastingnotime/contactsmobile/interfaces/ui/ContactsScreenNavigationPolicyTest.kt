package org.wastingnotime.contactsmobile.interfaces.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.unit.dp
import java.util.concurrent.atomic.AtomicBoolean
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.wastingnotime.contactsmobile.domain.Contact

class ContactsScreenNavigationPolicyTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun createSuccessReturnsToList() {
        val contact = Contact(
            id = "contact-1",
            firstName = "Ada",
            lastName = "Lovelace",
            phoneNumber = "5550100",
        )

        composeTestRule.setContent {
            TestSurface {
                ContactsScreen(
                    uiState = ContactsUiState.Loaded(contacts = listOf(contact)),
                    detailUiState = ContactDetailUiState.Hidden,
                    createUiState = CreateContactUiState.Success(contact),
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
                )
            }
        }

        composeTestRule.onNodeWithText("Back to list").assertIsDisplayed()
        composeTestRule.onAllNodesWithText("View contact").assertCountEquals(0)
    }

    @Test
    fun editSuccessReturnsToDetail() {
        val contact = Contact(
            id = "contact-2",
            firstName = "Grace",
            lastName = "Hopper",
            phoneNumber = "5550111",
        )

        composeTestRule.setContent {
            TestSurface {
                ContactsScreen(
                    uiState = ContactsUiState.Loaded(contacts = listOf(contact)),
                    detailUiState = ContactDetailUiState.Loaded(contact),
                    createUiState = CreateContactUiState.Hidden,
                    editUiState = EditContactUiState.Success(contact),
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
                    onEditSubmit = {},
                    onEditOpenContact = {},
                )
            }
        }

        composeTestRule.onNodeWithText("Back to detail").assertIsDisplayed()
        composeTestRule.onAllNodesWithText("View contact").assertCountEquals(0)
    }

    @Test
    fun deleteConfirmationRequiresAnExplicitDecision() {
        val contact = Contact(
            id = "contact-3",
            firstName = "Alan",
            lastName = "Turing",
            phoneNumber = "5550122",
        )
        val deleteInvoked = AtomicBoolean(false)

        composeTestRule.setContent {
            TestSurface {
                ContactsScreen(
                    uiState = ContactsUiState.Loaded(contacts = listOf(contact)),
                    detailUiState = ContactDetailUiState.Loaded(contact),
                    createUiState = CreateContactUiState.Hidden,
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
                    onDeleteContact = { deleteInvoked.set(true) },
                )
            }
        }

        composeTestRule.onNodeWithTag("detail-delete-button").performClick()
        composeTestRule.onNodeWithText("Delete contact?").assertIsDisplayed()
        composeTestRule.onNodeWithTag("delete-cancel-button").performClick()
        composeTestRule.onAllNodesWithText("Delete contact?").assertCountEquals(0)
        assertFalse(deleteInvoked.get())

        composeTestRule.onNodeWithTag("detail-delete-button").performClick()
        composeTestRule.onNodeWithText("Delete contact?").assertIsDisplayed()
        composeTestRule.onNodeWithTag("delete-confirm-button").performClick()

        composeTestRule.runOnIdle {
            assertTrue(deleteInvoked.get())
        }
    }
}

@Composable
private fun TestSurface(content: @Composable () -> Unit) {
    Box(
        modifier = Modifier
            .size(1080.dp, 1920.dp)
            .testTag("navigation-policy-surface"),
    ) {
        content()
    }
}
