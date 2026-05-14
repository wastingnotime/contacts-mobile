package org.wastingnotime.contactsmobile.interfaces.ui

import org.wastingnotime.contactsmobile.application.ContactsRepository
import org.wastingnotime.contactsmobile.application.LoadContactById
import org.wastingnotime.contactsmobile.application.LoadContacts
import org.wastingnotime.contactsmobile.domain.Contact
import org.wastingnotime.contactsmobile.test.MainDispatcherRule
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import java.util.ArrayDeque

@OptIn(ExperimentalCoroutinesApi::class)
class ContactsViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun `publishes loaded contacts after refresh`() = runTest {
        val contact = contact()
        val viewModel = ContactsViewModel(
            LoadContacts(
                ScriptedContactsRepository(
                    loadContactsResponses = arrayDequeOf(successContacts(listOf(contact))),
                ),
            ),
            LoadContactById(
                ScriptedContactsRepository(
                    loadContactsResponses = arrayDequeOf(successContacts(listOf(contact))),
                ),
            ),
        )

        advanceUntilIdle()

        assertEquals(
            ContactsUiState.Loaded(listOf(contact)),
            viewModel.uiState.value,
        )
    }

    @Test
    fun `preserves loaded contacts when refresh fails`() = runTest {
        val contact = contact()
        val viewModel = ContactsViewModel(
            LoadContacts(
                ScriptedContactsRepository(
                    loadContactsResponses = arrayDequeOf(
                        successContacts(listOf(contact)),
                        failingContacts("backend unavailable"),
                    ),
                ),
            ),
            LoadContactById(
                ScriptedContactsRepository(
                    loadContactsResponses = arrayDequeOf(successContacts(listOf(contact))),
                ),
            ),
        )

        advanceUntilIdle()

        viewModel.refresh()
        advanceUntilIdle()

        assertEquals(
            ContactsUiState.Loaded(
                contacts = listOf(contact),
                transientErrorMessage = "backend unavailable",
            ),
            viewModel.uiState.value,
        )
    }

    @Test
    fun `re-enters loading when retrying after a failure`() = runTest {
        val contact = contact()
        val gate = CompletableDeferred<Unit>()
        val repository = ScriptedContactsRepository(
            loadContactsResponses = arrayDequeOf(
                successContacts(listOf(contact)),
                failingContacts("backend unavailable"),
                blockingSuccessContacts(gate, listOf(contact)),
            ),
        )
        val viewModel = ContactsViewModel(
            LoadContacts(repository),
            LoadContactById(repository),
        )

        advanceUntilIdle()

        viewModel.refresh()
        advanceUntilIdle()
        assertEquals(
            ContactsUiState.Loaded(
                contacts = listOf(contact),
                transientErrorMessage = "backend unavailable",
            ),
            viewModel.uiState.value,
        )

        viewModel.refresh()
        assertEquals(ContactsUiState.Loading, viewModel.uiState.value)

        gate.complete(Unit)
        advanceUntilIdle()

        assertEquals(
            ContactsUiState.Loaded(listOf(contact)),
            viewModel.uiState.value,
        )
    }

    @Test
    fun `publishes an error when the initial load fails`() = runTest {
        val repository = ScriptedContactsRepository(
            loadContactsResponses = arrayDequeOf(failingContacts("backend unavailable")),
        )
        val viewModel = ContactsViewModel(
            LoadContacts(repository),
            LoadContactById(repository),
        )

        advanceUntilIdle()

        assertEquals(
            ContactsUiState.Error("backend unavailable"),
            viewModel.uiState.value,
        )
    }

    @Test
    fun `loads a contact detail when a contact is opened`() = runTest {
        val contact = contact()
        val repository = ScriptedContactsRepository(
            loadContactsResponses = arrayDequeOf(successContacts(listOf(contact))),
            loadContactByIdResponses = arrayDequeOf(successContact(contact)),
        )
        val viewModel = ContactsViewModel(
            LoadContacts(repository),
            LoadContactById(repository),
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
    fun `preserves loaded contact detail when detail refresh fails`() = runTest {
        val contact = contact()
        val repository = ScriptedContactsRepository(
            loadContactsResponses = arrayDequeOf(successContacts(listOf(contact))),
            loadContactByIdResponses = arrayDequeOf(
                successContact(contact),
                failingContact("backend unavailable"),
            ),
        )
        val viewModel = ContactsViewModel(
            LoadContacts(repository),
            LoadContactById(repository),
        )

        advanceUntilIdle()

        viewModel.openContact(contact)
        advanceUntilIdle()
        viewModel.refresh()
        advanceUntilIdle()

        assertEquals(
            ContactDetailUiState.Loaded(
                contact = contact,
                transientErrorMessage = "backend unavailable",
            ),
            viewModel.detailUiState.value,
        )
    }

    @Test
    fun `publishes not found when the opened contact is missing`() = runTest {
        val contact = contact()
        val repository = ScriptedContactsRepository(
            loadContactsResponses = arrayDequeOf(successContacts(listOf(contact))),
            loadContactByIdResponses = arrayDequeOf(missingContact()),
        )
        val viewModel = ContactsViewModel(
            LoadContacts(repository),
            LoadContactById(repository),
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
        val contact = contact()
        val repository = ScriptedContactsRepository(
            loadContactsResponses = arrayDequeOf(successContacts(listOf(contact))),
            loadContactByIdResponses = arrayDequeOf(successContact(contact)),
        )
        val viewModel = ContactsViewModel(
            LoadContacts(repository),
            LoadContactById(repository),
        )

        advanceUntilIdle()

        viewModel.openContact(contact)
        advanceUntilIdle()
        viewModel.closeContactDetail()

        assertEquals(ContactDetailUiState.Hidden, viewModel.detailUiState.value)
    }
}

private class ScriptedContactsRepository(
    private val loadContactsResponses: ArrayDeque<suspend () -> List<Contact>>,
    private val loadContactByIdResponses: ArrayDeque<suspend (String) -> Contact?> = ArrayDeque(),
) : ContactsRepository {
    override suspend fun loadContacts(): List<Contact> {
        return loadContactsResponses.removeFirst().invoke()
    }

    override suspend fun loadContactById(id: String): Contact? {
        val next = loadContactByIdResponses.pollFirst()
            ?: error("No scripted contact detail response available.")
        return next.invoke(id)
    }
}

private fun successContacts(contacts: List<Contact>): suspend () -> List<Contact> = { contacts }

private fun blockingSuccessContacts(
    gate: CompletableDeferred<Unit>,
    contacts: List<Contact>,
): suspend () -> List<Contact> = {
    gate.await()
    contacts
}

private fun failingContacts(message: String): suspend () -> List<Contact> = {
    error(message)
}

private fun successContact(contact: Contact): suspend (String) -> Contact? = { id ->
    contact.takeIf { it.id == id }
}

private fun failingContact(message: String): suspend (String) -> Contact? = {
    error(message)
}

private fun missingContact(): suspend (String) -> Contact? = {
    null
}

private fun <T> arrayDequeOf(vararg items: T): ArrayDeque<T> = ArrayDeque(items.asList())

private fun contact(): Contact = Contact(
    id = "contact-1",
    firstName = "Ada",
    lastName = "Lovelace",
    phoneNumber = "555-0100",
)
