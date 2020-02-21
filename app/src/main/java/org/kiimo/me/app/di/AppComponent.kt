package org.kiimo.me.app.di

import dagger.Component
import org.kiimo.me.main.repositories.MapRepository
import org.kiimo.me.service.network.client.KiimoAppClient
import org.kiimo.me.service.network.client.KiimoDeliverHttpClient
import org.kiimo.me.service.network.module.DeliveryHttpModule
import org.kiimo.me.service.network.module.NetworkModule
import org.kiimo.me.services.KiimoFirebaseMessagingService
import javax.inject.Singleton

@Singleton
@Component(modules = [ApplicationModule::class, NetworkModule::class, DeliveryHttpModule::class])
interface AppComponent {

    fun kiimoAppClient(): KiimoAppClient

    fun kiimoDeliveryHttpClient(): KiimoDeliverHttpClient

    fun inject(repository: MapRepository)

    fun inject(kiimoFirebaseMessagingService: KiimoFirebaseMessagingService)
}