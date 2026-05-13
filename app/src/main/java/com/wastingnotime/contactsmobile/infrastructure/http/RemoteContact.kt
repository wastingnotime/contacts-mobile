package com.wastingnotime.contactsmobile.infrastructure.http

import com.wastingnotime.contactsmobile.domain.Contact

data class RemoteContact(
    val id: String,
    val first_name: String,
    val last_name: String,
    val phone_number: String,
) {
    fun toDomain(): Contact = Contact(
        id = id,
        firstName = first_name,
        lastName = last_name,
        phoneNumber = phone_number,
    )
}
