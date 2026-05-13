package com.wastingnotime.contactsmobile.interfaces.ui

import com.wastingnotime.contactsmobile.application.ContactsRepository
import com.wastingnotime.contactsmobile.application.LoadContactById
import com.wastingnotime.contactsmobile.application.LoadContacts
import com.wastingnotime.contactsmobile.domain.Contact
import com.wastingnotime.contactsmobile.test.MainDispatcherRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ContactsViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun `publishes loaded contacts after refresh`() = runTest {
        val contact = Contact(
            id = "contact-1",
            firstName = "Ada",
            lastName = "Lovelace",
            phoneNumber = "555-0100",
        )
        val viewModel = ContactsViewModel(
            LoadContacts(FakeContactsRepository(listOf(contact))),
            LoadContactById(FakeContactsRepository(listOf(contact))),
        )

        advanceUntilIdle()

        assertEquals(
            ContactsUiState.Loaded(listOf(contact)),
            viewModel.uiState.value,
        )
    }

    @Test
    fun `publishes an error when refresh fails`() = runTest {
        val viewModel = ContactsViewModel(
            LoadContacts(FailingContactsRepository()),
            LoadContactById(FailingContactsRepository()),
        )

        advanceUntilIdle()

        assertTrue(viewModel.uiState.value is ContactsUiState.Error)
    }

    @Test
    fun `loads a contact detail when a contact is opened`() = runTest {
        val contact = Contact(
            id = "contact-1",
            firstName = "Ada",
            lastName = "Lovelace",
            phoneNumber = "555-0100",
        )
        val viewModel = ContactsViewModel(
            LoadContacts(FakeContactsRepository(listOf(contact))),
            LoadContactById(FakeContactsRepository(listOf(contact))),
        )

        advanceUntilIdle()

        viewModel.openContact(contact)
        advanceUntilIdle()

        assertEquals(
            ContactDetailUiState.Loaded(contact),
            viewModel.detailUiState.value,
        )
    }

    @Test
    fun `publishes not found when the opened contact is missing`() = runTest {
        val contact = Contact(
            id = "contact-1",
            firstName = "Ada",
            lastName = "Lovelace",
            phoneNumber = "555-0100",
        )
        val viewModel = ContactsViewModel(
            LoadContacts(FakeContactsRepository(listOf(contact))),
            LoadContactById(FakeContactsRepository(emptyList())),
        )

        advanceUntilIdle()

        viewModel.openContact(contact)
        advanceUntilIdle()

        assertEquals(
            ContactDetailUiState.NotFound(contact.id),
            viewModel.detailUiState.value,
        )
    }

    @Test
    fun `closes contact detail`() = runTest {
        val contact = Contact(
            id = "contact-1",
            firstName = "Ada",
            lastName = "Lovelace",
            phoneNumber = "555-0100",
        )
        val viewModel = ContactsViewModel(
            LoadContacts(FakeContactsRepository(listOf(contact))),
            LoadContactById(FakeContactsRepository(listOf(contact))),
        )

        advanceUntilIdle()

        viewModel.openContact(contact)
        advanceUntilIdle()
        viewModel.closeContactDetail()

        assertEquals(ContactDetailUiState.Hidden, viewModel.detailUiState.value)
    }

    @Test
    fun `refreshes the currently open detail view`() = runTest {
        val contact = Contact(
            id = "contact-1",
            firstName = "Ada",
            lastName = "Lovelace",
            phoneNumber = "555-0100",
        )
        val repository = CountingContactsRepository(contact)
        val viewModel = ContactsViewModel(
            LoadContacts(repository),
            LoadContactById(repository),
        )

        advanceUntilIdle()

        viewModel.openContact(contact)
        advanceUntilIdle()
        viewModel.refresh()
        advanceUntilIdle()

        assertEquals(ContactDetailUiState.Loaded(contact), viewModel.detailUiState.value)
        assertTrue(repository.contactLookups >= 2)
    }

    @Test
    fun `returns to hidden detail state after closing`() = runTest {
        val contact = Contact(
            id = "contact-1",
            firstName = "Ada",
            lastName = "Lovelace",
            phoneNumber = "555-0100",
        )
        val viewModel = ContactsViewModel(
            LoadContacts(FakeContactsRepository(listOf(contact))),
            LoadContactById(FakeContactsRepository(listOf(contact))),
        )

        advanceUntilIdle()

        viewModel.openContact(contact)
        advanceUntilIdle()
        viewModel.closeContactDetail()

        assertEquals(ContactDetailUiState.Hidden, viewModel.detailUiState.value)
    }
}

private class FakeContactsRepository(
    private val contacts: List<Contact>,
) : ContactsRepository {
    override suspend fun loadContacts(): List<Contact> = contacts

    override suspend fun loadContactById(id: String): Contact? = contacts.firstOrNull { it.id == id }
}

private class FailingContactsRepository : ContactsRepository {
    override suspend fun loadContacts(): List<Contact> {
        error("backend unavailable")
    }

    override suspend fun loadContactById(id: String): Contact? {
        error("backend unavailable")
    }
}

private class CountingContactsRepository(
    private val contact: Contact,
) : ContactsRepository {
    var contactLookups: Int = 0
        private set

    override suspend fun loadContacts(): List<Contact> = listOf(contact)

    override suspend fun loadContactById(id: String): Contact? {
        contactLookups += 1
        return contact.takeIf { it.id == id }
    }
}
