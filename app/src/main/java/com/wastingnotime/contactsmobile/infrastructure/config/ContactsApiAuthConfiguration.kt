package com.wastingnotime.contactsmobile.infrastructure.config

data class ContactsApiAuthConfiguration(
    val subject: String,
    val roles: String,
)

data class ContactsApiAuthHeaders(
    val subject: String,
    val roles: String,
) {
    fun applyTo(connection: java.net.HttpURLConnection) {
        connection.setRequestProperty("x-auth-subject", subject)
        connection.setRequestProperty("x-auth-roles", roles)
    }
}

object ContactsApiAuthHeadersResolver {
    fun resolve(configuration: ContactsApiAuthConfiguration): ContactsApiAuthHeaders {
        val subject = configuration.subject.trim()
        require(subject.isNotBlank()) {
            "contactsApiAuthSubject must not be blank."
        }

        val roles = configuration.roles.trim()
        require(roles.isNotBlank()) {
            "contactsApiAuthRoles must not be blank."
        }

        return ContactsApiAuthHeaders(
            subject = subject,
            roles = roles,
        )
    }
}
