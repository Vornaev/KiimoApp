package org.kiimo.me.register.di

import dagger.Component
import org.kiimo.me.app.di.AppComponent
import org.kiimo.me.app.di.FragmentScope
import org.kiimo.me.register.BaseRegistrationServicesActivity

@FragmentScope
@Component(
    dependencies = [AppComponent::class],
    modules = [RegisterAccountModule::class]
)
interface RegisterAccountUserComponent {
    fun injectRegistrationService(baseRegistrationServicesActivity: BaseRegistrationServicesActivity)
}