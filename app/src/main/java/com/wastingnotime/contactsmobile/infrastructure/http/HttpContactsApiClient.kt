package com.wastingnotime.contactsmobile.infrastructure.http

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL

class HttpContactsApiClient(
    private val baseUrl: String,
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

    private fun openConnection(): HttpURLConnection {
        val normalizedBaseUrl = baseUrl.trimEnd('/')
        val endpoint = URL("$normalizedBaseUrl/contacts")
        return (endpoint.openConnection() as HttpURLConnection).apply {
            requestMethod = "GET"
            connectTimeout = 5_000
            readTimeout = 5_000
            setRequestProperty("Accept", "application/json")
        }
    }
}
