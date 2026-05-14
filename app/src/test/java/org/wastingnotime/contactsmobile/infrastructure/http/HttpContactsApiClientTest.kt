package org.wastingnotime.contactsmobile.infrastructure.http

import org.wastingnotime.contactsmobile.infrastructure.config.ContactsApiAuthHeaders
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.InputStream
import java.io.OutputStream
import java.net.HttpURLConnection
import java.net.URL

class HttpContactsApiClientTest {
    @Test
    fun `attaches auth headers to list requests`() = runTest {
        val connection = RecordingHttpURLConnection(
            url = URL("http://example.com/contacts"),
            responseCodeValue = HttpURLConnection.HTTP_OK,
            responseBody = """
                [
                  {
                    "id": "contact-1",
                    "first_name": "Ada",
                    "last_name": "Lovelace",
                    "phone_number": "555-0100"
                  }
                ]
            """.trimIndent(),
        )
        val client = HttpContactsApiClient(
            baseUrl = "http://example.com",
            authHeaders = ContactsApiAuthHeaders(
                subject = "admin-user",
                roles = "admin",
            ),
            connectionFactory = { connection },
        )

        val contacts = client.fetchContacts()

        assertEquals("admin-user", connection.capturedRequestProperties["x-auth-subject"])
        assertEquals("admin", connection.capturedRequestProperties["x-auth-roles"])
        assertEquals(1, contacts.size)
    }

    @Test
    fun `attaches auth headers to detail requests`() = runTest {
        val connection = RecordingHttpURLConnection(
            url = URL("http://example.com/contacts/contact-1"),
            responseCodeValue = HttpURLConnection.HTTP_OK,
            responseBody = """
                {
                  "id": "contact-1",
                  "first_name": "Ada",
                  "last_name": "Lovelace",
                  "phone_number": "555-0100"
                }
            """.trimIndent(),
        )
        val client = HttpContactsApiClient(
            baseUrl = "http://example.com",
            authHeaders = ContactsApiAuthHeaders(
                subject = "admin-user",
                roles = "admin",
            ),
            connectionFactory = { connection },
        )

        val contact = client.fetchContactById("contact-1")

        assertEquals("admin-user", connection.capturedRequestProperties["x-auth-subject"])
        assertEquals("admin", connection.capturedRequestProperties["x-auth-roles"])
        assertEquals("contact-1", contact?.id)
    }

    @Test
    fun `attaches auth headers and body to create requests`() = runTest {
        val connection = RecordingHttpURLConnection(
            url = URL("http://example.com/contacts"),
            responseCodeValue = HttpURLConnection.HTTP_CREATED,
            responseBody = """
                {
                  "id": "contact-9",
                  "first_name": "Katherine",
                  "last_name": "Johnson",
                  "phone_number": "555-0199"
                }
            """.trimIndent(),
        )
        val client = HttpContactsApiClient(
            baseUrl = "http://example.com",
            authHeaders = ContactsApiAuthHeaders(
                subject = "admin-user",
                roles = "admin",
            ),
            connectionFactory = { connection },
        )

        val contact = client.createContact(
            firstName = "Katherine",
            lastName = "Johnson",
            phoneNumber = "555-0199",
        )

        assertEquals("POST", connection.requestMethodCaptured)
        assertEquals("admin-user", connection.capturedRequestProperties["x-auth-subject"])
        assertEquals("admin", connection.capturedRequestProperties["x-auth-roles"])
        assertEquals(
            """{"first_name":"Katherine","last_name":"Johnson","phone_number":"555-0199"}""",
            connection.capturedRequestBody,
        )
        assertEquals("contact-9", contact.id)
    }

    @Test
    fun `attaches auth headers and body to update requests`() = runTest {
        val connection = RecordingHttpURLConnection(
            url = URL("http://example.com/contacts/contact-9"),
            responseCodeValue = HttpURLConnection.HTTP_OK,
            responseBody = """
                {
                  "id": "contact-9",
                  "first_name": "Katherine",
                  "last_name": "Johnson",
                  "phone_number": "555-0110"
                }
            """.trimIndent(),
        )
        val client = HttpContactsApiClient(
            baseUrl = "http://example.com",
            authHeaders = ContactsApiAuthHeaders(
                subject = "admin-user",
                roles = "admin",
            ),
            connectionFactory = { connection },
        )

        val contact = client.updateContact(
            id = "contact-9",
            firstName = "Katherine",
            lastName = "Johnson",
            phoneNumber = "555-0110",
        )

        assertEquals("PUT", connection.requestMethodCaptured)
        assertEquals("admin-user", connection.capturedRequestProperties["x-auth-subject"])
        assertEquals("admin", connection.capturedRequestProperties["x-auth-roles"])
        assertEquals(
            """{"first_name":"Katherine","last_name":"Johnson","phone_number":"555-0110"}""",
            connection.capturedRequestBody,
        )
        assertEquals("contact-9", contact.id)
    }

    @Test
    fun `attaches auth headers to delete requests`() = runTest {
        val connection = RecordingHttpURLConnection(
            url = URL("http://example.com/contacts/contact-9"),
            responseCodeValue = HttpURLConnection.HTTP_NO_CONTENT,
            responseBody = "",
        )
        val client = HttpContactsApiClient(
            baseUrl = "http://example.com",
            authHeaders = ContactsApiAuthHeaders(
                subject = "admin-user",
                roles = "admin",
            ),
            connectionFactory = { connection },
        )

        client.deleteContact("contact-9")

        assertEquals("DELETE", connection.requestMethodCaptured)
        assertEquals("admin-user", connection.capturedRequestProperties["x-auth-subject"])
        assertEquals("admin", connection.capturedRequestProperties["x-auth-roles"])
    }
}

private class RecordingHttpURLConnection(
    url: URL,
    private val responseCodeValue: Int,
    private val responseBody: String,
) : HttpURLConnection(url) {
    val capturedRequestProperties = mutableMapOf<String, String>()
    var requestMethodCaptured: String? = null
    var capturedRequestBody: String? = null
    private val outputBuffer = ByteArrayOutputStream()

    override fun setRequestProperty(key: String?, value: String?) {
        if (key != null && value != null) {
            capturedRequestProperties[key] = value
        }
    }

    override fun setRequestMethod(method: String?) {
        requestMethodCaptured = method
        super.setRequestMethod(method)
    }

    override fun getResponseCode(): Int = responseCodeValue

    override fun getInputStream(): InputStream = ByteArrayInputStream(responseBody.toByteArray())

    override fun getOutputStream(): OutputStream = outputBuffer

    override fun disconnect() {
        capturedRequestBody = outputBuffer.toString()
    }

    override fun connect() = Unit

    override fun usingProxy(): Boolean = false
}
