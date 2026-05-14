package org.wastingnotime.contactsmobile.application

import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class DeleteContactTest {
    @Test
    fun `returns deleted after the repository deletes the contact`() = runTest {
        val repository = FakeDeleteContactsRepository()
        val useCase = DeleteContact(repository)

        val result = useCase.execute("contact-9")

        assertEquals(DeleteContactResult.Deleted, result)
        assertEquals(listOf("contact-9"), repository.deletedContactIds)
    }
}

private class FakeDeleteContactsRepository : ContactsRepository {
    val deletedContactIds = mutableListOf<String>()

    override suspend fun loadContacts() = emptyList<org.wastingnotime.contactsmobile.domain.Contact>()

    override suspend fun loadContactById(id: String) = null

    override suspend fun createContact(
        firstName: String,
        lastName: String,
        phoneNumber: String,
    ) = error("Create is not used in this test.")

    override suspend fun updateContact(
        id: String,
        firstName: String,
        lastName: String,
        phoneNumber: String,
    ) = error("Update is not used in this test.")

    override suspend fun deleteContact(id: String) {
        deletedContactIds += id
    }
}
