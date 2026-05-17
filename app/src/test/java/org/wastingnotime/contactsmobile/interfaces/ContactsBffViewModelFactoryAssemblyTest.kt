package org.wastingnotime.contactsmobile.interfaces

import org.junit.Assert.assertNotNull
import org.junit.Assert.assertEquals
import org.junit.Test

class ContactsBffViewModelFactoryAssemblyTest {
    @Test
    fun `assembles a contacts view model factory from use cases`() {
        val factory = ContactsBffViewModelFactoryAssembly.assemble(
            ContactsBffUseCaseAssembly.assemble(NoOpContactsRepository),
        )

        assertNotNull(factory)
        assertEquals("ContactsViewModelFactory", factory::class.simpleName)
    }
}
