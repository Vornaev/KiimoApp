package org.kiimo.me.main.menu.di

import androidx.lifecycle.ViewModelProviders
import dagger.Module
import dagger.Provides
import org.kiimo.me.app.BaseActivity
import org.kiimo.me.app.IBaseViewFeatures
import org.kiimo.me.app.di.BaseViewFeatureModule
import org.kiimo.me.main.menu.KiimoMainNavigationActivity
import org.kiimo.me.main.menu.mainViewModel.MainMenuViewModel
import org.kiimo.me.main.menu.mainViewModel.MainMenuViewModelFactory
import org.kiimo.me.main.menu.mainViewModel.MainViewModelRepository
import org.kiimo.me.register.service.RegisterAccountViewModel
import org.kiimo.me.service.network.client.KiimoAppClient
import org.kiimo.me.service.network.client.KiimoDeliverHttpClient

@Module(includes = [MainManiActivityModule::class, BaseViewFeatureModule::class])
class MainMenuModule() {

    @Provides
    fun mainMenuViewModel(
        activity: KiimoMainNavigationActivity,
        client: KiimoAppClient,
        deliverHttpClient: KiimoDeliverHttpClient,
        viewFeatures: IBaseViewFeatures
    ): MainMenuViewModel {
        return ViewModelProviders.of(
            activity,
            MainMenuViewModelFactory(
                MainViewModelRepository(
                    client,
                    deliverHttpClient,
                    viewFeatures
                )
            )
        )
            .get(MainMenuViewModel::class.java)
    }
}
