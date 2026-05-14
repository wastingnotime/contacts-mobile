package org.wastingnotime.contactsmobile.application

import org.wastingnotime.contactsmobile.domain.Contact

data class CreateContactCommand(
    val firstName: String,
    val lastName: String,
    val phoneNumber: String,
)

sealed interface CreateContactResult {
    data class Created(val contact: Contact) : CreateContactResult
}

class CreateContact(
    private val repository: ContactsRepository,
) {
    suspend fun execute(command: CreateContactCommand): CreateContactResult {
        val contact = repository.createContact(
            firstName = command.firstName,
            lastName = command.lastName,
            phoneNumber = command.phoneNumber,
        )
        return CreateContactResult.Created(contact)
    }
}
