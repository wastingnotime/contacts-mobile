package org.wastingnotime.contactsmobile.application

import org.wastingnotime.contactsmobile.domain.Contact

class SortContacts {
    fun execute(contacts: List<Contact>): List<Contact> {
        return contacts.sortedWith(
            compareBy<Contact> { sortKey(it.displayName) }
                .thenBy { it.id },
        )
    }

    private fun sortKey(displayName: String): String {
        val normalized = displayName.trim()
        return if (normalized.isBlank()) "~" else normalized.lowercase()
    }
}
