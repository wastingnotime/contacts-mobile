package org.wastingnotime.contactsmobile.interfaces

object ContactsBffAppStart {
    fun start(configuration: ContactsBffBootstrapConfiguration): ContactsBffBootstrap {
        return ContactsBffBootstrapper.build(configuration)
    }

    fun start(): ContactsBffBootstrap {
        return ContactsBffBootstrapper.build()
    }
}
