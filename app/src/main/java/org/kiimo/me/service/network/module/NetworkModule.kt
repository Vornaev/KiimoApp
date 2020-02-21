package org.kiimo.me.service.network.module

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import org.kiimo.me.app.ServerUrl
import org.kiimo.me.service.network.client.KiimoAppClient
import org.kiimo.me.service.network.client.KiimoDeliverHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton

@Module(includes = [(OkHttpClientModule::class)])
class NetworkModule() {


    @Provides
    @Singleton
    fun kiimoAppClient(@Named("authorizationRetrofit") retrofit: Retrofit): KiimoAppClient {
        return retrofit.create(KiimoAppClient::class.java)
    }


    @Provides
    @Singleton
    @Named("authorizationRetrofit")
    fun retrofit(okHttpClient: OkHttpClient, gsonConverterFactory: GsonConverterFactory): Retrofit {
        return Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(ServerUrl.BASE_AUTHORIZATION_URL)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(gsonConverterFactory)
            .build()
    }

    @Provides
    @Singleton
    fun gsonConverterFactory(gson: Gson): GsonConverterFactory {
        return GsonConverterFactory.create(gson)
    }

    @Provides
    @Singleton
    fun gson(): Gson {
        val gsonBuilder = GsonBuilder()
        return gsonBuilder.create()
    }
}