package org.kiimo.me.main.components

import dagger.Component
import org.kiimo.me.main.fragments.DeliveryMapFragment
import org.kiimo.me.main.fragments.ProfileFragment
import org.kiimo.me.main.modules.MapModule
import org.kiimo.me.main.modules.ProfileModule
import org.kiimo.me.main.viewmodels.MapViewModelFactory
import org.kiimo.me.main.viewmodels.ProfileViewModelFactory
import javax.inject.Singleton

@Singleton
@Component(modules = [ProfileModule::class, MapModule::class])
interface MainComponent {

    fun profileViewModelFactory(): ProfileViewModelFactory

    fun mapViewModelFactory(): MapViewModelFactory

    fun inject(fragment: ProfileFragment)

    fun inject(fragmentDelivery: DeliveryMapFragment)
}