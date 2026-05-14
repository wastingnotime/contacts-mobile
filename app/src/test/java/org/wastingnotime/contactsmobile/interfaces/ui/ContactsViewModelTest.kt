package org.wastingnotime.contactsmobile.interfaces.ui

import org.wastingnotime.contactsmobile.application.ContactsRepository
import org.wastingnotime.contactsmobile.application.CreateContact
import org.wastingnotime.contactsmobile.application.LoadContactById
import org.wastingnotime.contactsmobile.application.LoadContacts
import org.wastingnotime.contactsmobile.application.DeleteContact
import org.wastingnotime.contactsmobile.application.UpdateContact
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
            CreateContact(
                ScriptedContactsRepository(
                    loadContactsResponses = arrayDequeOf(successContacts(listOf(contact))),
                ),
            ),
            UpdateContact(
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
    fun `publishes loaded contacts in alphabetical order after refresh`() = runTest {
        val grace = contact().copy(
            id = "contact-2",
            firstName = "Grace",
            lastName = "Hopper",
            phoneNumber = "555-0200",
        )
        val ada = contact()
        val repository = ScriptedContactsRepository(
            loadContactsResponses = arrayDequeOf(successContacts(listOf(grace, ada))),
        )
        val viewModel = ContactsViewModel(
            LoadContacts(repository),
            LoadContactById(repository),
            CreateContact(repository),
            UpdateContact(repository),
            DeleteContact(repository),
        )

        advanceUntilIdle()

        assertEquals(
            ContactsUiState.Loaded(listOf(ada, grace)),
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
            CreateContact(
                ScriptedContactsRepository(
                    loadContactsResponses = arrayDequeOf(successContacts(listOf(contact))),
                ),
            ),
            UpdateContact(
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
            CreateContact(repository),
            UpdateContact(repository),
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
            CreateContact(repository),
            UpdateContact(repository),
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
            CreateContact(repository),
            UpdateContact(repository),
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
            CreateContact(repository),
            UpdateContact(repository),
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
            CreateContact(repository),
            UpdateContact(repository),
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
            CreateContact(repository),
            UpdateContact(repository),
        )

        advanceUntilIdle()

        viewModel.openContact(contact)
        advanceUntilIdle()
        viewModel.closeContactDetail()

        assertEquals(ContactDetailUiState.Hidden, viewModel.detailUiState.value)
    }

    @Test
    fun `requires create form fields before submission`() = runTest {
        val viewModel = ContactsViewModel(
            LoadContacts(ScriptedContactsRepository(arrayDequeOf(successContacts(emptyList())))),
            LoadContactById(ScriptedContactsRepository(arrayDequeOf(successContacts(emptyList())))),
            CreateContact(ScriptedContactsRepository(arrayDequeOf(successContacts(emptyList())))),
            UpdateContact(ScriptedContactsRepository(arrayDequeOf(successContacts(emptyList())))),
        )

        advanceUntilIdle()

        viewModel.openCreateContact()
        viewModel.submitCreateContact()

        assertEquals(
            CreateContactUiState.Form(
                CreateContactFormState(
                    errorMessage = "Please complete the required fields.",
                    fieldErrors = mapOf(
                        CreateContactField.FirstName to "First name is required.",
                        CreateContactField.LastName to "Last name is required.",
                        CreateContactField.PhoneNumber to "Phone number is required.",
                    ),
                ),
            ),
            viewModel.createUiState.value,
        )
    }

    @Test
    fun `creates a contact and exposes it for detail navigation`() = runTest {
        val contact = contact().copy(id = "contact-9")
        val repository = ScriptedContactsRepository(
            loadContactsResponses = arrayDequeOf(successContacts(listOf(contact()))),
            loadContactByIdResponses = arrayDequeOf(successContact(contact)),
            createContactResponses = arrayDequeOf(successCreatedContact(contact)),
        )
        val viewModel = ContactsViewModel(
            LoadContacts(repository),
            LoadContactById(repository),
            CreateContact(repository),
            UpdateContact(repository),
        )

        advanceUntilIdle()

        viewModel.openCreateContact()
        viewModel.updateCreateFirstName(contact.firstName)
        viewModel.updateCreateLastName(contact.lastName)
        viewModel.updateCreatePhoneNumber(contact.phoneNumber)
        viewModel.submitCreateContact()
        advanceUntilIdle()

        assertEquals(
            CreateContactUiState.Success(contact),
            viewModel.createUiState.value,
        )

        viewModel.openCreatedContact()
        advanceUntilIdle()

        assertEquals(
            ContactDetailUiState.Loaded(contact),
            viewModel.detailUiState.value,
        )
    }

    @Test
    fun `prefills edit form from the loaded detail`() = runTest {
        val contact = contact()
        val repository = ScriptedContactsRepository(
            loadContactsResponses = arrayDequeOf(successContacts(listOf(contact))),
            loadContactByIdResponses = arrayDequeOf(successContact(contact)),
        )
        val viewModel = ContactsViewModel(
            LoadContacts(repository),
            LoadContactById(repository),
            CreateContact(repository),
            UpdateContact(repository),
        )

        advanceUntilIdle()

        viewModel.openContact(contact)
        advanceUntilIdle()
        viewModel.openEditContact()

        assertEquals(
            EditContactUiState.Form(
                EditContactFormState(
                    contactId = contact.id,
                    firstName = contact.firstName,
                    lastName = contact.lastName,
                    phoneNumber = contact.phoneNumber,
                ),
            ),
            viewModel.editUiState.value,
        )
    }

    @Test
    fun `updates a contact and reflects the saved values back into detail`() = runTest {
        val original = contact()
        val updated = original.copy(
            firstName = "Ada",
            lastName = "Byron",
            phoneNumber = "555-0199",
        )
        val repository = ScriptedContactsRepository(
            loadContactsResponses = arrayDequeOf(successContacts(listOf(original))),
            loadContactByIdResponses = arrayDequeOf(successContact(original), successContact(updated)),
            updateContactResponses = arrayDequeOf(successUpdatedContact(updated)),
        )
        val viewModel = ContactsViewModel(
            LoadContacts(repository),
            LoadContactById(repository),
            CreateContact(repository),
            UpdateContact(repository),
        )

        advanceUntilIdle()

        viewModel.openContact(original)
        advanceUntilIdle()
        viewModel.openEditContact()
        viewModel.updateEditFirstName(updated.firstName)
        viewModel.updateEditLastName(updated.lastName)
        viewModel.updateEditPhoneNumber(updated.phoneNumber)
        viewModel.submitEditContact()
        advanceUntilIdle()

        assertEquals(
            EditContactUiState.Success(updated),
            viewModel.editUiState.value,
        )
        assertEquals(
            ContactsUiState.Loaded(listOf(updated)),
            viewModel.uiState.value,
        )

        viewModel.openUpdatedContact()
        advanceUntilIdle()

        assertEquals(
            ContactDetailUiState.Loaded(updated),
            viewModel.detailUiState.value,
        )
    }

    @Test
    fun `deletes a contact and returns to the list`() = runTest {
        val contact = contact()
        val repository = ScriptedContactsRepository(
            loadContactsResponses = arrayDequeOf(successContacts(listOf(contact))),
            loadContactByIdResponses = arrayDequeOf(successContact(contact)),
            deleteContactResponses = arrayDequeOf(successDeleteContact()),
        )
        val viewModel = ContactsViewModel(
            LoadContacts(repository),
            LoadContactById(repository),
            CreateContact(repository),
            UpdateContact(repository),
            DeleteContact(repository),
        )

        advanceUntilIdle()

        viewModel.openContact(contact)
        advanceUntilIdle()
        viewModel.deleteContact()
        advanceUntilIdle()

        assertEquals(ContactsUiState.Empty(), viewModel.uiState.value)
        assertEquals(ContactDetailUiState.Hidden, viewModel.detailUiState.value)
    }

    @Test
    fun `preserves the selected contact when delete fails`() = runTest {
        val contact = contact()
        val repository = ScriptedContactsRepository(
            loadContactsResponses = arrayDequeOf(successContacts(listOf(contact))),
            loadContactByIdResponses = arrayDequeOf(successContact(contact)),
            deleteContactResponses = arrayDequeOf(failingDeleteContact("backend unavailable")),
        )
        val viewModel = ContactsViewModel(
            LoadContacts(repository),
            LoadContactById(repository),
            CreateContact(repository),
            UpdateContact(repository),
            DeleteContact(repository),
        )

        advanceUntilIdle()

        viewModel.openContact(contact)
        advanceUntilIdle()
        viewModel.deleteContact()
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
    fun `filters loaded contacts when the search query matches`() = runTest {
        val matching = contact()
        val other = contact().copy(
            id = "contact-2",
            firstName = "Grace",
            lastName = "Hopper",
            phoneNumber = "555-0200",
        )
        val repository = ScriptedContactsRepository(
            loadContactsResponses = arrayDequeOf(successContacts(listOf(matching, other))),
        )
        val viewModel = ContactsViewModel(
            LoadContacts(repository),
            LoadContactById(repository),
            CreateContact(repository),
            UpdateContact(repository),
            DeleteContact(repository),
        )

        advanceUntilIdle()

        viewModel.updateSearchQuery("ada")

        assertEquals(
            ContactsUiState.Loaded(
                contacts = listOf(matching),
                searchQuery = "ada",
            ),
            viewModel.uiState.value,
        )
    }

    @Test
    fun `shows a filtered empty state when the search query matches nothing`() = runTest {
        val repository = ScriptedContactsRepository(
            loadContactsResponses = arrayDequeOf(successContacts(listOf(contact()))),
        )
        val viewModel = ContactsViewModel(
            LoadContacts(repository),
            LoadContactById(repository),
            CreateContact(repository),
            UpdateContact(repository),
            DeleteContact(repository),
        )

        advanceUntilIdle()

        viewModel.updateSearchQuery("zebra")

        assertEquals(
            ContactsUiState.FilteredEmpty("zebra"),
            viewModel.uiState.value,
        )
    }

    @Test
    fun `clears the search query and restores the full list`() = runTest {
        val matching = contact()
        val other = contact().copy(
            id = "contact-2",
            firstName = "Grace",
            lastName = "Hopper",
            phoneNumber = "555-0200",
        )
        val repository = ScriptedContactsRepository(
            loadContactsResponses = arrayDequeOf(successContacts(listOf(matching, other))),
        )
        val viewModel = ContactsViewModel(
            LoadContacts(repository),
            LoadContactById(repository),
            CreateContact(repository),
            UpdateContact(repository),
            DeleteContact(repository),
        )

        advanceUntilIdle()

        viewModel.updateSearchQuery("ada")
        assertEquals(
            ContactsUiState.Loaded(
                contacts = listOf(matching),
                searchQuery = "ada",
            ),
            viewModel.uiState.value,
        )

        viewModel.updateSearchQuery("")

        assertEquals(
            ContactsUiState.Loaded(listOf(matching, other)),
            viewModel.uiState.value,
        )
    }

    @Test
    fun `keeps the filtered match count visible in loaded search results`() = runTest {
        val matching = contact()
        val other = contact().copy(
            id = "contact-2",
            firstName = "Grace",
            lastName = "Hopper",
            phoneNumber = "555-0200",
        )
        val repository = ScriptedContactsRepository(
            loadContactsResponses = arrayDequeOf(successContacts(listOf(matching, other))),
        )
        val viewModel = ContactsViewModel(
            LoadContacts(repository),
            LoadContactById(repository),
            CreateContact(repository),
            UpdateContact(repository),
            DeleteContact(repository),
        )

        advanceUntilIdle()

        viewModel.updateSearchQuery("ada")

        assertEquals(
            ContactsUiState.Loaded(
                contacts = listOf(matching),
                searchQuery = "ada",
            ),
            viewModel.uiState.value,
        )
    }

    @Test
    fun `preserves the search query when navigating to detail and back`() = runTest {
        val contact = contact()
        val repository = ScriptedContactsRepository(
            loadContactsResponses = arrayDequeOf(successContacts(listOf(contact))),
            loadContactByIdResponses = arrayDequeOf(successContact(contact)),
        )
        val viewModel = ContactsViewModel(
            LoadContacts(repository),
            LoadContactById(repository),
            CreateContact(repository),
            UpdateContact(repository),
            DeleteContact(repository),
        )

        advanceUntilIdle()

        viewModel.updateSearchQuery("ada")
        assertEquals(
            ContactsUiState.Loaded(
                contacts = listOf(contact),
                searchQuery = "ada",
            ),
            viewModel.uiState.value,
        )

        viewModel.openContact(contact)
        advanceUntilIdle()

        assertEquals(
            ContactsUiState.Loaded(
                contacts = listOf(contact),
                searchQuery = "ada",
            ),
            viewModel.uiState.value,
        )
        assertEquals(
            ContactDetailUiState.Loaded(contact),
            viewModel.detailUiState.value,
        )

        viewModel.closeContactDetail()

        assertEquals(
            ContactsUiState.Loaded(
                contacts = listOf(contact),
                searchQuery = "ada",
            ),
            viewModel.uiState.value,
        )
    }

    @Test
    fun `preserves the search query when opening and closing forms`() = runTest {
        val contact = contact()
        val repository = ScriptedContactsRepository(
            loadContactsResponses = arrayDequeOf(successContacts(listOf(contact))),
            loadContactByIdResponses = arrayDequeOf(successContact(contact)),
        )
        val viewModel = ContactsViewModel(
            LoadContacts(repository),
            LoadContactById(repository),
            CreateContact(repository),
            UpdateContact(repository),
            DeleteContact(repository),
        )

        advanceUntilIdle()

        viewModel.updateSearchQuery("ada")
        viewModel.openCreateContact()

        assertEquals(
            ContactsUiState.Loaded(
                contacts = listOf(contact),
                searchQuery = "ada",
            ),
            viewModel.uiState.value,
        )
        assertEquals(
            CreateContactUiState.Form(CreateContactFormState()),
            viewModel.createUiState.value,
        )

        viewModel.closeCreateContact()
        viewModel.openContact(contact)
        advanceUntilIdle()
        viewModel.openEditContact()

        assertEquals(
            ContactsUiState.Loaded(
                contacts = listOf(contact),
                searchQuery = "ada",
            ),
            viewModel.uiState.value,
        )
        assertEquals(
            EditContactUiState.Form(
                EditContactFormState(
                    contactId = contact.id,
                    firstName = contact.firstName,
                    lastName = contact.lastName,
                    phoneNumber = contact.phoneNumber,
                ),
            ),
            viewModel.editUiState.value,
        )

        viewModel.closeEditContact()

        assertEquals(
            ContactsUiState.Loaded(
                contacts = listOf(contact),
                searchQuery = "ada",
            ),
            viewModel.uiState.value,
        )
    }
}

private class ScriptedContactsRepository(
    private val loadContactsResponses: ArrayDeque<suspend () -> List<Contact>>,
    private val loadContactByIdResponses: ArrayDeque<suspend (String) -> Contact?> = ArrayDeque(),
    private val createContactResponses: ArrayDeque<suspend (String, String, String) -> Contact> = ArrayDeque(),
    private val updateContactResponses: ArrayDeque<suspend (String, String, String, String) -> Contact> = ArrayDeque(),
    private val deleteContactResponses: ArrayDeque<suspend (String) -> Unit> = ArrayDeque(),
) : ContactsRepository {
    override suspend fun loadContacts(): List<Contact> {
        return loadContactsResponses.removeFirst().invoke()
    }

    override suspend fun loadContactById(id: String): Contact? {
        val next = loadContactByIdResponses.pollFirst()
            ?: error("No scripted contact detail response available.")
        return next.invoke(id)
    }

    override suspend fun createContact(
        firstName: String,
        lastName: String,
        phoneNumber: String,
    ): Contact {
        val next = createContactResponses.pollFirst()
            ?: error("No scripted create contact response available.")
        return next.invoke(firstName, lastName, phoneNumber)
    }

    override suspend fun updateContact(
        id: String,
        firstName: String,
        lastName: String,
        phoneNumber: String,
    ): Contact {
        val next = updateContactResponses.pollFirst()
            ?: error("No scripted update contact response available.")
        return next.invoke(id, firstName, lastName, phoneNumber)
    }

    override suspend fun deleteContact(id: String) {
        val next = deleteContactResponses.pollFirst()
            ?: error("No scripted delete contact response available.")
        next.invoke(id)
    }
}

private fun successDeleteContact(): suspend (String) -> Unit = { _ -> }

private fun failingDeleteContact(message: String): suspend (String) -> Unit = {
    error(message)
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

private fun successCreatedContact(contact: Contact): suspend (String, String, String) -> Contact = { _, _, _ ->
    contact
}

private fun successUpdatedContact(contact: Contact): suspend (String, String, String, String) -> Contact = { _, _, _, _ ->
    contact
}

private fun <T> arrayDequeOf(vararg items: T): ArrayDeque<T> = ArrayDeque(items.asList())

private fun contact(): Contact = Contact(
    id = "contact-1",
    firstName = "Ada",
    lastName = "Lovelace",
    phoneNumber = "555-0100",
)
