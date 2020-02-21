package org.kiimo.me.main.modules

import dagger.Module
import dagger.Provides
import org.kiimo.me.main.repositories.MapRepository
import org.kiimo.me.main.viewmodels.MapViewModelFactory
import javax.inject.Singleton

@Module
class MapModule {

    @Provides
    @Singleton
    fun providesMapViewModelFactory(repository: MapRepository): MapViewModelFactory =
        MapViewModelFactory(repository)

    @Provides
    @Singleton
    fun mapsRepository(): MapRepository = MapRepository.getInstance()
}