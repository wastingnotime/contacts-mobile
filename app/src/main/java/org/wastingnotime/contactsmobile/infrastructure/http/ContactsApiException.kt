package org.wastingnotime.contactsmobile.infrastructure.http

import java.io.IOException

class ContactsApiException(
    message: String,
    cause: Throwable? = null,
) : IOException(message, cause)
