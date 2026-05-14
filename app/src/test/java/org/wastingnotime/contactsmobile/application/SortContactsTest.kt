package org.wastingnotime.contactsmobile.application

import org.junit.Assert.assertEquals
import org.junit.Test
import org.wastingnotime.contactsmobile.domain.Contact

class SortContactsTest {
    @Test
    fun `sorts contacts alphabetically by display name`() {
        val contacts = listOf(
            contact(id = "contact-2", firstName = "Grace", lastName = "Hopper"),
            contact(id = "contact-1", firstName = "Ada", lastName = "Lovelace"),
            contact(id = "contact-3", firstName = "Katherine", lastName = "Johnson"),
        )
        val sortContacts = SortContacts()

        val results = sortContacts.execute(contacts)

        assertEquals(
            listOf(
                contacts[1],
                contacts[0],
                contacts[2],
            ),
            results,
        )
    }

    @Test
    fun `uses the contact id as a deterministic tie breaker`() {
        val first = contact(id = "contact-2", firstName = "Ada", lastName = "Lovelace")
        val second = contact(id = "contact-1", firstName = "Ada", lastName = "Lovelace")
        val sortContacts = SortContacts()

        val results = sortContacts.execute(listOf(first, second))

        assertEquals(listOf(second, first), results)
    }

    private fun contact(
        id: String,
        firstName: String,
        lastName: String,
    ): Contact {
        return Contact(
            id = id,
            firstName = firstName,
            lastName = lastName,
            phoneNumber = "555-0100",
        )
    }
}
