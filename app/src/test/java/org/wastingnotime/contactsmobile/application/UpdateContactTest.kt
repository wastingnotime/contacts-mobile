package org.wastingnotime.contactsmobile.application

import org.wastingnotime.contactsmobile.domain.Contact
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class UpdateContactTest {
    @Test
    fun `returns the updated contact from the repository`() = runTest {
        val repository = FakeUpdateContactsRepository(
            updatedContact = Contact(
                id = "contact-9",
                firstName = "Ada",
                lastName = "Byron",
                phoneNumber = "555-0199",
            ),
        )
        val useCase = UpdateContact(repository)

        val result = useCase.execute(
            UpdateContactCommand(
                id = "contact-9",
                firstName = "Ada",
                lastName = "Byron",
                phoneNumber = "555-0199",
            ),
        )

        assertEquals(
            UpdateContactResult.Updated(
                Contact(
                    id = "contact-9",
                    firstName = "Ada",
                    lastName = "Byron",
                    phoneNumber = "555-0199",
                ),
            ),
            result,
        )
    }
}

private class FakeUpdateContactsRepository(
    private val updatedContact: Contact,
) : ContactsRepository {
    override suspend fun loadContacts(): List<Contact> = emptyList()

    override suspend fun loadContactById(id: String): Contact? = null

    override suspend fun createContact(
        firstName: String,
        lastName: String,
        phoneNumber: String,
    ): Contact = error("Create is not used in this test.")

    override suspend fun updateContact(
        id: String,
        firstName: String,
        lastName: String,
        phoneNumber: String,
    ): Contact = updatedContact
}
