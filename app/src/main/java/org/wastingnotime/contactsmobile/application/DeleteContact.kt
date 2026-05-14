package org.wastingnotime.contactsmobile.application

sealed interface DeleteContactResult {
    data object Deleted : DeleteContactResult
}

class DeleteContact(
    private val repository: ContactsRepository,
) {
    suspend fun execute(id: String): DeleteContactResult {
        repository.deleteContact(id)
        return DeleteContactResult.Deleted
    }
}
