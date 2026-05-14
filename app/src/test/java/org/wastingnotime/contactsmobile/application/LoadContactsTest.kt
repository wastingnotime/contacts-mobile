package org.wastingnotime.contactsmobile.application

import org.wastingnotime.contactsmobile.domain.Contact
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class LoadContactsTest {
    @Test
    fun `returns empty when repository returns no contacts`() = runTest {
        val useCase = LoadContacts(FakeContactsRepository(emptyList()))

        val result = useCase.execute()

        assertTrue(result is LoadContactsResult.Empty)
    }

    @Test
    fun `returns loaded contacts when repository has data`() = runTest {
        val contacts = listOf(
            Contact(
                id = "contact-1",
                firstName = "Ada",
                lastName = "Lovelace",
                phoneNumber = "555-0100",
            ),
        )
        val useCase = LoadContacts(FakeContactsRepository(contacts))

        val result = useCase.execute()

        assertEquals(LoadContactsResult.Loaded(contacts), result)
    }
}

private class FakeContactsRepository(
    private val contacts: List<Contact>,
) : ContactsRepository {
    override suspend fun loadContacts(): List<Contact> = contacts

    override suspend fun loadContactById(id: String): Contact? = contacts.firstOrNull { it.id == id }

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
    ): Contact = error("Update is not used in this test.")
}
