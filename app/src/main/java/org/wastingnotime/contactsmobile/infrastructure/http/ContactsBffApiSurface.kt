package org.wastingnotime.contactsmobile.infrastructure.http

import java.net.URLEncoder

data class ContactsBffApiSurfaceConfiguration(
    val apiPrefix: String,
)

class ContactsBffApiSurface private constructor(
    private val apiPrefix: String,
) {
    fun contactsList(): String {
        return "$apiPrefix/contacts"
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

    companion object {
        fun from(configuration: ContactsBffApiSurfaceConfiguration): ContactsBffApiSurface {
            val rawPrefix = configuration.apiPrefix.trim()
            require(rawPrefix.isNotBlank()) {
                "contactsBffApiPrefix must not be blank."
            }
            val normalizedPrefix = normalizePrefix(rawPrefix)
            return ContactsBffApiSurface(normalizedPrefix)
        }

        private fun normalizePrefix(value: String): String {
            val trimmed = value.trimEnd('/')
            return if (trimmed.startsWith("/")) trimmed else "/$trimmed"
        }
    }

    private fun encodePathSegment(value: String): String {
        return URLEncoder.encode(value, Charsets.UTF_8.name())
    }
}

object ContactsBffApiSurfaceResolver {
    fun resolve(configuration: ContactsBffApiSurfaceConfiguration): ContactsBffApiSurface {
        return ContactsBffApiSurface.from(configuration)
    }
}
