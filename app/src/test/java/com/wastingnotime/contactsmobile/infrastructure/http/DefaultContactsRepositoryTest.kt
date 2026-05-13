package com.wastingnotime.contactsmobile.infrastructure.http

import com.wastingnotime.contactsmobile.domain.Contact
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class DefaultContactsRepositoryTest {
    @Test
    fun `maps remote contacts into domain contacts`() = runTest {
        val repository = DefaultContactsRepository(
            apiClient = FakeContactsApiClient(
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
            apiClient = FakeContactsApiClient(
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
            apiClient = FakeContactsApiClient(emptyList()),
        )

        val contact = repository.loadContactById("missing-contact")

        assertEquals(null, contact)
    }
}

private class FakeContactsApiClient(
    private val contacts: List<RemoteContact>,
) : ContactsApiClient {
    override suspend fun fetchContacts(): List<RemoteContact> = contacts

    override suspend fun fetchContactById(id: String): RemoteContact? = contacts.firstOrNull { it.id == id }
}
