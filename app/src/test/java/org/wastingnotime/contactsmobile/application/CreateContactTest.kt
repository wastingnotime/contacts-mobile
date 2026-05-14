package org.wastingnotime.contactsmobile.application

import org.wastingnotime.contactsmobile.domain.Contact
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class CreateContactTest {
    @Test
    fun `returns the created contact from the repository`() = runTest {
        val repository = FakeCreateContactsRepository(
            createdContact = Contact(
                id = "contact-9",
                firstName = "Katherine",
                lastName = "Johnson",
                phoneNumber = "555-0199",
            ),
        )
        val useCase = CreateContact(repository)

        val result = useCase.execute(
            CreateContactCommand(
                firstName = "Katherine",
                lastName = "Johnson",
                phoneNumber = "555-0199",
            ),
        )

        assertEquals(
            CreateContactResult.Created(
                Contact(
                    id = "contact-9",
                    firstName = "Katherine",
                    lastName = "Johnson",
                    phoneNumber = "555-0199",
                ),
            ),
            result,
        )
    }
}

private class FakeCreateContactsRepository(
    private val createdContact: Contact,
) : ContactsRepository {
    override suspend fun loadContacts(): List<Contact> = emptyList()

    override suspend fun loadContactById(id: String): Contact? = null

    override suspend fun createContact(
        firstName: String,
        lastName: String,
        phoneNumber: String,
    ): Contact = createdContact
}
