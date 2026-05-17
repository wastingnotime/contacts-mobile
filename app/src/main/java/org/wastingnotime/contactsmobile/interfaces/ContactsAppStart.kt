package org.wastingnotime.contactsmobile.interfaces

object ContactsAppStart {
    fun bootstrap(configuration: ContactsBffBootstrapConfiguration): ContactsBffBootstrap {
        return ContactsBffBootstrapper.build(configuration)
    }

    fun bootstrap(): ContactsBffBootstrap {
        return ContactsBffBootstrapper.build()
    }
}
