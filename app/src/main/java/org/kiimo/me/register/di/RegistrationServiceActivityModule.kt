package org.kiimo.me.register.di

import dagger.Module
import dagger.Provides
import org.kiimo.me.register.BaseRegistrationServicesActivity

@Module
class RegistrationServiceActivityModule(private val activity: BaseRegistrationServicesActivity)
{

    @Provides
    fun registrationServicesActivity() = activity
}