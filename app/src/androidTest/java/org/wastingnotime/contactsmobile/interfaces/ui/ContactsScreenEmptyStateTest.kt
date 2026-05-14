package org.wastingnotime.contactsmobile.interfaces.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.unit.dp
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import kotlin.math.abs

class ContactsScreenEmptyStateTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun centersTheEmptyStateContentHorizontally() {
        composeTestRule.setContent {
            TestSurface {
                ContactsScreen(
                    uiState = ContactsUiState.Empty(),
                    detailUiState = ContactDetailUiState.Hidden,
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
                )
            }
        }

        val rootBounds = composeTestRule.onNodeWithTag("empty-surface").fetchSemanticsNode().boundsInRoot
        val titleBounds = composeTestRule.onNodeWithText("No contacts yet").fetchSemanticsNode().boundsInRoot
        val bodyBounds = composeTestRule.onNodeWithText("The backend returned an empty list.").fetchSemanticsNode().boundsInRoot
        val actionBounds = composeTestRule.onNodeWithText("Reload").fetchSemanticsNode().boundsInRoot

        assertCentered(rootBounds, titleBounds)
        assertCentered(rootBounds, bodyBounds)
        assertCentered(rootBounds, actionBounds)
        composeTestRule.onNodeWithText("No contacts yet").assertIsDisplayed()
    }

    @Test
    fun centersTheFilteredEmptyStateContentHorizontally() {
        composeTestRule.setContent {
            TestSurface {
                ContactsScreen(
                    uiState = ContactsUiState.FilteredEmpty(query = "zebra"),
                    detailUiState = ContactDetailUiState.Hidden,
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
                    onSearchQueryChange = {},
                )
            }
        }

        val rootBounds = composeTestRule.onNodeWithTag("empty-surface").fetchSemanticsNode().boundsInRoot
        val titleBounds = composeTestRule.onNodeWithText("No matching contacts").fetchSemanticsNode().boundsInRoot
        val bodyBounds = composeTestRule.onNodeWithText("No loaded contacts match \"zebra\".").fetchSemanticsNode().boundsInRoot
        val actionBounds = composeTestRule.onNodeWithText("Clear search").fetchSemanticsNode().boundsInRoot

        assertCentered(rootBounds, titleBounds)
        assertCentered(rootBounds, bodyBounds)
        assertCentered(rootBounds, actionBounds)
        composeTestRule.onNodeWithText("No matching contacts").assertIsDisplayed()
    }

    @Test
    fun centersTheErrorStateContentHorizontally() {
        composeTestRule.setContent {
            TestSurface {
                ContactsScreen(
                    uiState = ContactsUiState.Error(message = "Unable to load contacts."),
                    detailUiState = ContactDetailUiState.Hidden,
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
                )
            }
        }

        val rootBounds = composeTestRule.onNodeWithTag("empty-surface").fetchSemanticsNode().boundsInRoot
        val titleBounds = composeTestRule.onNodeWithText("Unable to load contacts").fetchSemanticsNode().boundsInRoot
        val bodyBounds = composeTestRule.onNodeWithText("Unable to load contacts.").fetchSemanticsNode().boundsInRoot
        val actionBounds = composeTestRule.onNodeWithText("Retry").fetchSemanticsNode().boundsInRoot

        assertCentered(rootBounds, titleBounds)
        assertCentered(rootBounds, bodyBounds)
        assertCentered(rootBounds, actionBounds)
        composeTestRule.onNodeWithText("Unable to load contacts").assertIsDisplayed()
    }

    private fun assertCentered(rootBounds: Rect, childBounds: Rect) {
        val rootCenterX = rootBounds.left + (rootBounds.width / 2f)
        val childCenterX = childBounds.left + (childBounds.width / 2f)
        assertTrue(
            "Expected horizontal centering within 12px, but root center was $rootCenterX and child center was $childCenterX.",
            abs(rootCenterX - childCenterX) <= 12f,
        )
    }
}

@Composable
private fun TestSurface(content: @Composable () -> Unit) {
    Box(
        modifier = Modifier
            .size(1080.dp, 1920.dp)
            .testTag("empty-surface"),
    ) {
        content()
    }
}
