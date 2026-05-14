package org.wastingnotime.contactsmobile.interfaces.ui

import org.junit.Assert.assertEquals
import org.junit.Test

class ContactsListViewportStateTest {
    @Test
    fun `keeps the anchor contact visible inside a filtered result set`() {
        val contacts = listOf(
            contact(id = "contact-2"),
            contact(id = "contact-3"),
            contact(id = "contact-4"),
        )
        val viewportState = ContactsListViewportState(
            firstVisibleItemIndex = 2,
            firstVisibleItemScrollOffset = 18,
            anchorContactId = "contact-3",
            secondaryAnchorContactId = "contact-4",
        )

        assertEquals(1, viewportState.resolveVisibleIndex(contacts))
    }

    @Test
    fun `falls back to the secondary anchor when the primary anchor disappears`() {
        val contacts = listOf(
            contact(id = "contact-1"),
            contact(id = "contact-2"),
            contact(id = "contact-3"),
        )
        val viewportState = ContactsListViewportState(
            firstVisibleItemIndex = 2,
            firstVisibleItemScrollOffset = 18,
            anchorContactId = "missing-contact",
            secondaryAnchorContactId = "contact-2",
        )

        assertEquals(1, viewportState.resolveVisibleIndex(contacts))
    }

    @Test
    fun `clamps to the last available row when no anchors survive`() {
        val contacts = listOf(
            contact(id = "contact-1"),
            contact(id = "contact-2"),
        )
        val viewportState = ContactsListViewportState(
            firstVisibleItemIndex = 7,
            firstVisibleItemScrollOffset = 18,
            anchorContactId = "missing-contact",
            secondaryAnchorContactId = "also-missing",
        )

        assertEquals(1, viewportState.resolveVisibleIndex(contacts))
    }

    @Test
    fun `aligns the remembered viewport to the surviving search results`() {
        val contacts = listOf(
            contact(id = "contact-2"),
            contact(id = "contact-3"),
            contact(id = "contact-4"),
        )
        val viewportState = ContactsListViewportState(
            firstVisibleItemIndex = 2,
            firstVisibleItemScrollOffset = 18,
            anchorContactId = "contact-3",
            secondaryAnchorContactId = "contact-4",
        )

        assertEquals(
            ContactsListViewportState(
                firstVisibleItemIndex = 1,
                firstVisibleItemScrollOffset = 18,
                anchorContactId = "contact-3",
                secondaryAnchorContactId = "contact-4",
            ),
            viewportState.alignTo(contacts),
        )
    }

    private fun contact(id: String) = org.wastingnotime.contactsmobile.domain.Contact(
        id = id,
        firstName = "Ada",
        lastName = "Lovelace",
        phoneNumber = "555-0100",
    )
}
