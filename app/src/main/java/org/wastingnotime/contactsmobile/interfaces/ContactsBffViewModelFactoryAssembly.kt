package org.wastingnotime.contactsmobile.interfaces

import org.wastingnotime.contactsmobile.interfaces.ui.ContactsViewModelFactory

object ContactsBffViewModelFactoryAssembly {
    fun assemble(useCases: ContactsBffUseCases): ContactsViewModelFactory {
        return ContactsViewModelFactory(
            useCases.loadContacts,
            useCases.loadContactById,
            useCases.createContact,
            useCases.updateContact,
            useCases.deleteContact,
        )
    }
}
