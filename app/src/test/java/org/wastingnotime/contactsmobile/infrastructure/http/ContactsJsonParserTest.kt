package org.wastingnotime.contactsmobile.infrastructure.http

import org.junit.Assert.assertEquals
import org.junit.Test

class ContactsJsonParserTest {
    @Test
    fun `parses snake case contacts payload`() {
        val payload = """
            [
              {
                "id": "contact-1",
                "first_name": "Ada",
                "last_name": "Lovelace",
                "phone_number": "555-0100"
              }
            ]
        """.trimIndent()

        val contacts = ContactsJsonParser.parseContacts(payload)

        assertEquals(
            listOf(
                RemoteContact(
                    id = "contact-1",
                    first_name = "Ada",
                    last_name = "Lovelace",
                    phone_number = "555-0100",
                ),
            ),
            contacts,
        )
    }

    @Test
    fun `parses a single contact payload`() {
        val payload = """
            {
              "id": "contact-1",
              "first_name": "Ada",
              "last_name": "Lovelace",
              "phone_number": "555-0100"
            }
        """.trimIndent()

        val contact = ContactsJsonParser.parseContact(payload)

        assertEquals(
            RemoteContact(
                id = "contact-1",
                first_name = "Ada",
                last_name = "Lovelace",
                phone_number = "555-0100",
            ),
            contact,
        )
    }
}
