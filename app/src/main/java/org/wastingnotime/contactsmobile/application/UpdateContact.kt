package org.wastingnotime.contactsmobile.application

import org.wastingnotime.contactsmobile.domain.Contact

data class UpdateContactCommand(
    val id: String,
    val firstName: String,
    val lastName: String,
    val phoneNumber: String,
)

sealed interface UpdateContactResult {
    data class Updated(val contact: Contact) : UpdateContactResult
}

class UpdateContact(
    private val repository: ContactsRepository,
) {
    suspend fun execute(command: UpdateContactCommand): UpdateContactResult {
        val contact = repository.updateContact(
            id = command.id,
            firstName = command.firstName,
            lastName = command.lastName,
            phoneNumber = command.phoneNumber,
        )
        return UpdateContactResult.Updated(contact)
    }
}
