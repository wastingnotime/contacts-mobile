package org.wastingnotime.contactsmobile.interfaces

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test

class ContactsBootstrapAssemblyTest {
    @Test
    fun `assembles the final contacts bootstrap from a view model factory`() {
        val bootstrap = ContactsBootstrapAssembly.assemble(
            ContactsBffViewModelFactoryAssembly.assemble(
                ContactsBffUseCaseAssembly.assemble(NoOpContactsRepository),
            ),
        )

        assertNotNull(bootstrap.viewModelFactory)
        assertEquals("ContactsBootstrap", bootstrap::class.simpleName)
        assertEquals("ContactsViewModelFactory", bootstrap.viewModelFactory::class.simpleName)
    }
}
