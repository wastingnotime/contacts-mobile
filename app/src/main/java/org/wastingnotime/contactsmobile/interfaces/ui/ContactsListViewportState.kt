package org.wastingnotime.contactsmobile.interfaces.ui

data class ContactsListViewportState(
    val firstVisibleItemIndex: Int = 0,
    val firstVisibleItemScrollOffset: Int = 0,
    val anchorContactId: String? = null,
    val secondaryAnchorContactId: String? = null,
)

fun ContactsListViewportState.resolveVisibleIndex(contacts: List<org.wastingnotime.contactsmobile.domain.Contact>): Int {
    if (contacts.isEmpty()) {
        return 0
    }

    val candidateIds = listOfNotNull(anchorContactId, secondaryAnchorContactId)
    for (candidateId in candidateIds) {
        val candidateIndex = contacts.indexOfFirst { it.id == candidateId }
        if (candidateIndex >= 0) {
            return candidateIndex
        }
    }

    return firstVisibleItemIndex.coerceIn(0, contacts.lastIndex)
}

fun ContactsListViewportState.alignTo(
    contacts: List<org.wastingnotime.contactsmobile.domain.Contact>,
): ContactsListViewportState {
    if (contacts.isEmpty()) {
        return copy(firstVisibleItemIndex = 0)
    }

    return copy(firstVisibleItemIndex = resolveVisibleIndex(contacts))
}
