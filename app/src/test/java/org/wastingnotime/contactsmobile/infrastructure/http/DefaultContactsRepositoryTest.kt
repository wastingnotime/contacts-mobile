package org.wastingnotime.contactsmobile.infrastructure.http

import org.wastingnotime.contactsmobile.domain.Contact
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class DefaultContactsRepositoryTest {
    @Test
    fun `maps remote contacts into domain contacts`() = runTest {
        val repository = DefaultContactsRepository(
            apiClient = FakeContactsBffClient(
                listOf(
                    RemoteContact(
                        id = "contact-1",
                        first_name = "Ada",
                        last_name = "Lovelace",
                        phone_number = "555-0100",
                    ),
                ),
            ),
        )

        val contacts = repository.loadContacts()

        assertEquals(
            listOf(
                Contact(
                    id = "contact-1",
                    firstName = "Ada",
                    lastName = "Lovelace",
                    phoneNumber = "555-0100",
                ),
            ),
            contacts,
        )
    }

    @Test
    fun `maps a remote contact by id into a domain contact`() = runTest {
        val repository = DefaultContactsRepository(
            apiClient = FakeContactsBffClient(
                listOf(
                    RemoteContact(
                        id = "contact-1",
                        first_name = "Ada",
                        last_name = "Lovelace",
                        phone_number = "555-0100",
                    ),
                ),
            ),
        )

        val contact = repository.loadContactById("contact-1")

        assertEquals(
            Contact(
                id = "contact-1",
                firstName = "Ada",
                lastName = "Lovelace",
                phoneNumber = "555-0100",
            ),
            contact,
        )
    }

    @Test
    fun `returns null when a remote contact is missing`() = runTest {
        val repository = DefaultContactsRepository(
            apiClient = FakeContactsBffClient(emptyList()),
        )

        val contact = repository.loadContactById("missing-contact")

        assertEquals(null, contact)
    }

    @Test
    fun `maps a created remote contact into a domain contact`() = runTest {
        val repository = DefaultContactsRepository(
            apiClient = FakeContactsBffClient(
                contacts = emptyList(),
                createdContact = RemoteContact(
                    id = "contact-3",
                    first_name = "Katherine",
                    last_name = "Johnson",
                    phone_number = "555-0199",
                ),
            ),
        )

        val contact = repository.createContact(
            firstName = "Katherine",
            lastName = "Johnson",
            phoneNumber = "555-0199",
        )

        assertEquals(
            Contact(
                id = "contact-3",
                firstName = "Katherine",
                lastName = "Johnson",
                phoneNumber = "555-0199",
            ),
            contact,
        )
    }

    @Test
    fun `maps an updated remote contact into a domain contact`() = runTest {
        val repository = DefaultContactsRepository(
            apiClient = FakeContactsBffClient(
                contacts = emptyList(),
                updatedContact = RemoteContact(
                    id = "contact-3",
                    first_name = "Katherine",
                    last_name = "Johnson",
                    phone_number = "555-0110",
                ),
            ),
        )

        val contact = repository.updateContact(
            id = "contact-3",
            firstName = "Katherine",
            lastName = "Johnson",
            phoneNumber = "555-0110",
        )

        assertEquals(
            Contact(
                id = "contact-3",
                firstName = "Katherine",
                lastName = "Johnson",
                phoneNumber = "555-0110",
            ),
            contact,
        )
    }
}

private class FakeContactsBffClient(
    private val contacts: List<RemoteContact>,
    private val createdContact: RemoteContact? = null,
    private val updatedContact: RemoteContact? = null,
) : ContactsBffClient {
    override suspend fun fetchContacts(): List<RemoteContact> = contacts

    override suspend fun fetchContactById(id: String): RemoteContact? = contacts.firstOrNull { it.id == id }

    override suspend fun createContact(
        firstName: String,
        lastName: String,
        phoneNumber: String,
    ): RemoteContact {
        return createdContact ?: error("No scripted create contact response available.")
    }

    override suspend fun updateContact(
        id: String,
        firstName: String,
        lastName: String,
        phoneNumber: String,
    ): RemoteContact {
        return updatedContact ?: error("No scripted update contact response available.")
    }

    override suspend fun deleteContact(id: String) = Unit
}
