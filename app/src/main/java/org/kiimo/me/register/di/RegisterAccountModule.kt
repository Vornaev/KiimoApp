package org.kiimo.me.register.di

import androidx.lifecycle.ViewModelProviders
import dagger.Module
import dagger.Provides
import org.kiimo.me.app.IBaseViewFeatures
import org.kiimo.me.app.di.BaseViewFeatureModule
import org.kiimo.me.register.BaseRegistrationServicesActivity
import org.kiimo.me.register.RegisterSyncAccountActivity
import org.kiimo.me.register.service.RegisterAccountRepository
import org.kiimo.me.register.service.RegisterAccountViewModel
import org.kiimo.me.register.service.RegsiterViewModelFactory
import org.kiimo.me.service.network.client.KiimoAppClient
import org.kiimo.me.service.network.client.KiimoDeliverHttpClient


@Module(includes = [RegistrationServiceActivityModule::class, BaseViewFeatureModule::class])
class RegisterAccountModule {

    @Provides
    fun viewModel(
        activity: BaseRegistrationServicesActivity,
        client: KiimoAppClient,
        deliverHttpClient: KiimoDeliverHttpClient,
        viewFeatures: IBaseViewFeatures
    ): RegisterAccountViewModel {
        return ViewModelProviders.of(
            activity,
            RegsiterViewModelFactory(RegisterAccountRepository(client,deliverHttpClient ,viewFeatures))
        )
            .get(RegisterAccountViewModel::class.java)
    }
}