package org.wastingnotime.contactsmobile.infrastructure.config

data class ContactsBffAuthConfiguration(
    val subject: String,
    val roles: String,
)

data class ContactsBffAuthHeaders(
    val subject: String,
    val roles: String,
) {
    fun applyTo(connection: java.net.HttpURLConnection) {
        connection.setRequestProperty("x-auth-subject", subject)
        connection.setRequestProperty("x-auth-roles", roles)
    }
}

object ContactsBffAuthHeadersResolver {
    fun resolve(configuration: ContactsBffAuthConfiguration): ContactsBffAuthHeaders {
        val subject = configuration.subject.trim()
        require(subject.isNotBlank()) {
            "contactsBffAuthSubject must not be blank."
        }

        val roles = configuration.roles.trim()
        require(roles.isNotBlank()) {
            "contactsBffAuthRoles must not be blank."
        }

        return ContactsBffAuthHeaders(
            subject = subject,
            roles = roles,
        )
    }
}
