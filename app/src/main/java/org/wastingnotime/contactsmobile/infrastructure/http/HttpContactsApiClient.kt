package org.wastingnotime.contactsmobile.infrastructure.http

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder
import org.wastingnotime.contactsmobile.infrastructure.config.ContactsApiAuthHeaders

class HttpContactsApiClient(
    private val baseUrl: String,
    private val authHeaders: ContactsApiAuthHeaders,
    private val connectionFactory: (URL) -> HttpURLConnection = { it.openConnection() as HttpURLConnection },
) : ContactsApiClient {
    override suspend fun fetchContacts(): List<RemoteContact> = withContext(Dispatchers.IO) {
        val connection = openConnection()
        try {
            val statusCode = connection.responseCode
            if (statusCode !in 200..299) {
                throw ContactsApiException("Contacts API responded with status $statusCode.")
            }
            val body = connection.inputStream.bufferedReader().use { it.readText() }
            ContactsJsonParser.parseContacts(body)
        } catch (exception: ContactsApiException) {
            throw exception
        } catch (exception: IOException) {
            throw ContactsApiException("Unable to load contacts.", exception)
        } finally {
            connection.disconnect()
        }
    }

    override suspend fun fetchContactById(id: String): RemoteContact? = withContext(Dispatchers.IO) {
        val connection = openConnection("contacts/${encodePathSegment(id)}")
        try {
            val statusCode = connection.responseCode
            when {
                statusCode == HttpURLConnection.HTTP_NOT_FOUND -> null
                statusCode !in 200..299 -> throw ContactsApiException("Contacts API responded with status $statusCode.")
                else -> {
                    val body = connection.inputStream.bufferedReader().use { it.readText() }
                    ContactsJsonParser.parseContact(body)
                }
            }
        } catch (exception: ContactsApiException) {
            throw exception
        } catch (exception: IOException) {
            throw ContactsApiException("Unable to load contact.", exception)
        } finally {
            connection.disconnect()
        }
    }

    private fun openConnection(): HttpURLConnection {
        return openConnection("contacts")
    }

    private fun openConnection(path: String): HttpURLConnection {
        val normalizedBaseUrl = baseUrl.trimEnd('/')
        val endpoint = URL("$normalizedBaseUrl/$path")
        return connectionFactory(endpoint).apply {
            requestMethod = "GET"
            connectTimeout = 5_000
            readTimeout = 5_000
            setRequestProperty("Accept", "application/json")
            authHeaders.applyTo(this)
        }
    }

    private fun encodePathSegment(value: String): String {
        return URLEncoder.encode(value, Charsets.UTF_8.name())
    }
}
