package org.kiimo.me.main.menu.di

import dagger.Component
import org.kiimo.me.app.BaseActivity
import org.kiimo.me.app.di.AppComponent
import org.kiimo.me.app.di.FragmentScope
import org.kiimo.me.main.menu.KiimoMainNavigationActivity


@FragmentScope
@Component(dependencies = [AppComponent::class], modules = [MainMenuModule::class])
interface MainMenuComponent {

    fun inject(activity: KiimoMainNavigationActivity)
}