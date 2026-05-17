package org.wastingnotime.contactsmobile.interfaces

import org.wastingnotime.contactsmobile.application.ContactsRepository
import org.wastingnotime.contactsmobile.application.CreateContact
import org.wastingnotime.contactsmobile.application.DeleteContact
import org.wastingnotime.contactsmobile.application.LoadContactById
import org.wastingnotime.contactsmobile.application.LoadContacts
import org.wastingnotime.contactsmobile.application.UpdateContact

data class ContactsBffUseCases(
    val loadContacts: LoadContacts,
    val loadContactById: LoadContactById,
    val createContact: CreateContact,
    val updateContact: UpdateContact,
    val deleteContact: DeleteContact,
)

object ContactsBffUseCaseAssembly {
    fun assemble(repository: ContactsRepository): ContactsBffUseCases {
        return ContactsBffUseCases(
            loadContacts = LoadContacts(repository),
            loadContactById = LoadContactById(repository),
            createContact = CreateContact(repository),
            updateContact = UpdateContact(repository),
            deleteContact = DeleteContact(repository),
        )
    }
}
