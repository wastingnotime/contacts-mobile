package org.wastingnotime.contactsmobile.interfaces

object ContactsAppStart {
    fun bootstrap(): ContactsBffBootstrap {
        return ContactsBffBootstrapper.build()
    }
}
