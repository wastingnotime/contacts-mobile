package org.wastingnotime.contactsmobile.interfaces

object ContactsAppStart {
    fun bootstrap(): ContactsBootstrap {
        return ContactsBffBootstrapper.build()
    }
}
