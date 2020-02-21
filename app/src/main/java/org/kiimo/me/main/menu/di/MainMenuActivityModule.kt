package org.kiimo.me.main.menu.di

import dagger.Module
import dagger.Provides
import org.kiimo.me.app.BaseActivity
import org.kiimo.me.main.menu.KiimoMainNavigationActivity

@Module
class MainManiActivityModule(private val activity: KiimoMainNavigationActivity) {

    @Provides
    fun getMainNavActivity() = activity
}