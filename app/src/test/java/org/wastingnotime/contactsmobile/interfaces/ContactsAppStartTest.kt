package org.wastingnotime.contactsmobile.interfaces

import org.junit.Assert.assertNotNull
import org.junit.Test

class ContactsAppStartTest {
    @Test
    fun `bootstraps the app through the single app start entry point`() {
        val bootstrap = ContactsAppStart.bootstrap()

        assertNotNull(bootstrap.viewModelFactory)
    }
}
