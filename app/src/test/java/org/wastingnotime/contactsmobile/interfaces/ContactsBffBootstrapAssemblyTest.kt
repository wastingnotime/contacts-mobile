package org.wastingnotime.contactsmobile.interfaces

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test

class ContactsBffBootstrapAssemblyTest {
    @Test
    fun `assembles the final contacts bff bootstrap from a view model factory`() {
        val bootstrap = ContactsBffBootstrapAssembly.assemble(
            ContactsBffViewModelFactoryAssembly.assemble(
                ContactsBffUseCaseAssembly.assemble(NoOpContactsRepository),
            ),
        )

        assertNotNull(bootstrap.viewModelFactory)
        assertEquals("ContactsViewModelFactory", bootstrap.viewModelFactory::class.simpleName)
    }
}
