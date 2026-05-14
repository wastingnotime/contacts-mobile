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

class ContactsScreenCreateFormImeTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun advancesThroughTheCreateFormFieldsAndSubmitsFromPhoneNumber() {
        val submitted = AtomicBoolean(false)

        composeTestRule.setContent {
            TestSurface {
                ContactsScreen(
                    uiState = ContactsUiState.Loaded(contacts = emptyList()),
                    detailUiState = ContactDetailUiState.Hidden,
                    createUiState = CreateContactUiState.Form(CreateContactFormState()),
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
                    onCreateSubmit = { submitted.set(true) },
                    onCreateOpenContact = {},
                )
            }
        }

        composeTestRule.onNodeWithTag("create-first-name").performTextInput("Ada")
        composeTestRule.onNodeWithTag("create-first-name").performImeAction()
        composeTestRule.onNodeWithTag("create-last-name").assertIsFocused()

        composeTestRule.onNodeWithTag("create-last-name").performTextInput("Lovelace")
        composeTestRule.onNodeWithTag("create-last-name").performImeAction()
        composeTestRule.onNodeWithTag("create-phone-number").assertIsFocused()

        composeTestRule.onNodeWithTag("create-phone-number").performTextInput("5550100")
        composeTestRule.onNodeWithTag("create-phone-number").performImeAction()

        composeTestRule.runOnIdle {
            assertTrue("Expected the phone-number field IME action to submit the create form.", submitted.get())
        }
    }
}

@Composable
private fun TestSurface(content: @Composable () -> Unit) {
    Box(
        modifier = Modifier
            .size(1080.dp, 1920.dp)
            .testTag("create-form-surface"),
    ) {
        content()
    }
}
