package org.wastingnotime.contactsmobile.application

import org.wastingnotime.contactsmobile.domain.Contact

class FilterContacts {
    fun execute(contacts: List<Contact>, query: String): List<Contact> {
        val normalizedQuery = query.trim()
        if (normalizedQuery.isEmpty()) {
            return contacts
        }

        val loweredQuery = normalizedQuery.lowercase()
        return contacts.filter { contact ->
            contact.matchesQuery(loweredQuery)
        }
    }

    private fun Contact.matchesQuery(loweredQuery: String): Boolean {
        return listOf(
            id,
            firstName,
            lastName,
            displayName,
            phoneNumber,
        ).any { value ->
            value.contains(loweredQuery, ignoreCase = true)
        }
    }
}
