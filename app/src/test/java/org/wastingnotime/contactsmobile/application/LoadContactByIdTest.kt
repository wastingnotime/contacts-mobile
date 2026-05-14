package org.wastingnotime.contactsmobile.application

import org.wastingnotime.contactsmobile.domain.Contact
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class LoadContactByIdTest {
    @Test
    fun `returns loaded contact when repository finds the contact`() = runTest {
        val contact = Contact(
            id = "contact-1",
            firstName = "Ada",
            lastName = "Lovelace",
            phoneNumber = "555-0100",
        )
        val useCase = LoadContactById(ContactByIdRepositoryFake(contact))

        val result = useCase.execute("contact-1")

        assertEquals(LoadContactByIdResult.Loaded(contact), result)
    }

    @Test
    fun `returns not found when repository does not find the contact`() = runTest {
        val useCase = LoadContactById(ContactByIdRepositoryFake(null))

        val result = useCase.execute("missing-contact")

        assertTrue(result is LoadContactByIdResult.NotFound)
    }
}

private class ContactByIdRepositoryFake(
    private val contact: Contact?,
) : ContactsRepository {
    override suspend fun loadContacts(): List<Contact> = contact?.let { listOf(it) } ?: emptyList()

    override suspend fun loadContactById(id: String): Contact? = contact?.takeIf { it.id == id }

    override suspend fun createContact(
        firstName: String,
        lastName: String,
        phoneNumber: String,
    ): Contact = error("Create is not used in this test.")
}
