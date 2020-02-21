package org.kiimo.me.service.network.module

import org.kiimo.me.service.network.module.OkHttpClientModule

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import org.kiimo.me.app.ServerUrl
import org.kiimo.me.service.network.client.KiimoDeliverHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton

@Module(includes = [(OkHttpClientModule::class)])
class DeliveryHttpModule(val baseUrl: String) {


    @Provides
    @Singleton
    fun kiimoAppDeliveryClient(@Named("deliveryRetrofit") retrofit: Retrofit): KiimoDeliverHttpClient {
        return retrofit.create(KiimoDeliverHttpClient::class.java)
    }



    @Provides
    @Singleton
    @Named("deliveryRetrofit")
    fun retrofitDelivery(okHttpClient: OkHttpClient, gsonConverterFactory: GsonConverterFactory): Retrofit {
        return Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(ServerUrl.BASE_DELIVERY_URL)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(gsonConverterFactory)
            .build()
    }


}

