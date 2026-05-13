package com.wastingnotime.contactsmobile.interfaces.ui

import com.wastingnotime.contactsmobile.application.ContactsRepository
import com.wastingnotime.contactsmobile.application.LoadContacts
import com.wastingnotime.contactsmobile.domain.Contact
import com.wastingnotime.contactsmobile.test.MainDispatcherRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ContactsViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun `publishes loaded contacts after refresh`() = runTest {
        val viewModel = ContactsViewModel(
            LoadContacts(
                object : ContactsRepository {
                    override suspend fun loadContacts(): List<Contact> {
                        return listOf(
                            Contact(
                                id = "contact-1",
                                firstName = "Ada",
                                lastName = "Lovelace",
                                phoneNumber = "555-0100",
                            ),
                        )
                    }
                },
            ),
        )

        advanceUntilIdle()

        assertEquals(
            ContactsUiState.Loaded(
                listOf(
                    Contact(
                        id = "contact-1",
                        firstName = "Ada",
                        lastName = "Lovelace",
                        phoneNumber = "555-0100",
                    ),
                ),
            ),
            viewModel.uiState.value,
        )
    }

    @Test
    fun `publishes an error when refresh fails`() = runTest {
        val viewModel = ContactsViewModel(
            LoadContacts(
                object : ContactsRepository {
                    override suspend fun loadContacts(): List<Contact> {
                        error("backend unavailable")
                    }
                },
            ),
        )

        advanceUntilIdle()

        assertTrue(viewModel.uiState.value is ContactsUiState.Error)
    }

    @Test
    fun `opens and closes contact detail`() = runTest {
        val viewModel = ContactsViewModel(
            LoadContacts(
                object : ContactsRepository {
                    override suspend fun loadContacts(): List<Contact> {
                        return listOf(
                            Contact(
                                id = "contact-1",
                                firstName = "Ada",
                                lastName = "Lovelace",
                                phoneNumber = "555-0100",
                            ),
                        )
                    }
                },
            ),
        )
        advanceUntilIdle()

        val contact = (viewModel.uiState.value as ContactsUiState.Loaded).contacts.single()

        viewModel.openContact(contact)
        assertEquals(contact, viewModel.selectedContact.value)

        viewModel.closeContactDetail()
        assertNull(viewModel.selectedContact.value)
    }
}
