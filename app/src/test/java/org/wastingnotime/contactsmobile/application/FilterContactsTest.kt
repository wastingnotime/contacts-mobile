package org.wastingnotime.contactsmobile.application

import org.junit.Assert.assertEquals
import org.junit.Test
import org.wastingnotime.contactsmobile.domain.Contact

class FilterContactsTest {
    @Test
    fun `returns matching contacts for a case insensitive query`() {
        val contacts = listOf(
            contact(id = "contact-1", firstName = "Ada", lastName = "Lovelace", phoneNumber = "555-0100"),
            contact(id = "contact-2", firstName = "Grace", lastName = "Hopper", phoneNumber = "555-0200"),
        )
        val filterContacts = FilterContacts()

        val results = filterContacts.execute(contacts, "lov")

        assertEquals(listOf(contacts.first()), results)
    }

    @Test
    fun `returns all contacts when the query is blank`() {
        val contacts = listOf(
            contact(id = "contact-1", firstName = "Ada", lastName = "Lovelace", phoneNumber = "555-0100"),
            contact(id = "contact-2", firstName = "Grace", lastName = "Hopper", phoneNumber = "555-0200"),
        )
        val filterContacts = FilterContacts()

        val results = filterContacts.execute(contacts, "   ")

        assertEquals(contacts, results)
    }

    @Test
    fun `does not match on internal id alone`() {
        val contacts = listOf(
            contact(id = "contact-1", firstName = "Ada", lastName = "Lovelace", phoneNumber = "555-0100"),
        )
        val filterContacts = FilterContacts()

        val results = filterContacts.execute(contacts, "contact-1")

        assertEquals(emptyList<Contact>(), results)
    }

    private fun contact(
        id: String,
        firstName: String,
        lastName: String,
        phoneNumber: String,
    ): Contact {
        return Contact(
            id = id,
            firstName = firstName,
            lastName = lastName,
            phoneNumber = phoneNumber,
        )
    }
}
