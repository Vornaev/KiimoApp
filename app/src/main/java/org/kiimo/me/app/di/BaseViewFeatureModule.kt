package org.kiimo.me.app.di

import dagger.Module
import dagger.Provides
import org.kiimo.me.app.IBaseViewFeatures


@Module
class BaseViewFeatureModule (val baseViewFeatureModule: IBaseViewFeatures){

    @Provides
    fun getBaseViewFeature() = baseViewFeatureModule
}