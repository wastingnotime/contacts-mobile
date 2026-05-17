package org.wastingnotime.contactsmobile.infrastructure.http

import java.net.URLEncoder

object ContactsBffRoutes {
    private const val apiPrefix = "/api"
    private const val contactsSegment = "contacts"

    fun contactsList(): String {
        return "$apiPrefix/$contactsSegment"
    }

    fun contactById(id: String): String {
        return "${contactsList()}/${encodePathSegment(id)}"
    }

    fun createContact(): String {
        return contactsList()
    }

    fun updateContact(id: String): String {
        return contactById(id)
    }

    fun deleteContact(id: String): String {
        return contactById(id)
    }

    private fun encodePathSegment(value: String): String {
        return URLEncoder.encode(value, Charsets.UTF_8.name())
    }
}
