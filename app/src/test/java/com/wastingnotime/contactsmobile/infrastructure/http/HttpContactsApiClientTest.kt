package com.wastingnotime.contactsmobile.infrastructure.http

import com.wastingnotime.contactsmobile.infrastructure.config.ContactsApiAuthHeaders
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test
import java.io.ByteArrayInputStream
import java.io.InputStream
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
}

private class RecordingHttpURLConnection(
    url: URL,
    private val responseCodeValue: Int,
    private val responseBody: String,
) : HttpURLConnection(url) {
    val capturedRequestProperties = mutableMapOf<String, String>()

    override fun setRequestProperty(key: String?, value: String?) {
        if (key != null && value != null) {
            capturedRequestProperties[key] = value
        }
    }

    override fun getResponseCode(): Int = responseCodeValue

    override fun getInputStream(): InputStream = ByteArrayInputStream(responseBody.toByteArray())

    override fun connect() = Unit

    override fun disconnect() = Unit

    override fun usingProxy(): Boolean = false
}
