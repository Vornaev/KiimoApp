package org.kiimo.me.service.network.module

import android.content.Context
import android.util.Log
import dagger.Module
import dagger.Provides
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.kiimo.me.app.di.ApplicationModule
import org.kiimo.me.service.network.interceptors.ConnectivityInterceptor
import org.kiimo.me.service.network.interceptors.UserAgentInterceptor
import java.io.File
import java.util.concurrent.TimeUnit
import javax.inject.Singleton


@Module(includes = [(ApplicationModule::class)])
class OkHttpClientModule {

    companion object {
        const val HTTP_CACHE = "HttpCache"
        const val CONNECT_TIMEOUT = 15L
        const val READ_TIMEOUT = 15L
        val USER_AGENT = "Android Kiimo"
    }

    @Provides
    @Singleton
    fun okHttpClient(cache: okhttp3.Cache, httpLoggingInterceptor: HttpLoggingInterceptor, userAgentInterceptor: UserAgentInterceptor, context: Context): OkHttpClient {

        val builder = OkHttpClient.Builder()
                .cache(cache)
                .addInterceptor(ConnectivityInterceptor(context))
                .addNetworkInterceptor(httpLoggingInterceptor)
                .addNetworkInterceptor(userAgentInterceptor)
                .connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)


        return builder.build()
    }

    @Provides
    @Singleton
    fun httpLoggingInterceptor(): HttpLoggingInterceptor {
        val httpLoggingInterceptor = HttpLoggingInterceptor(HttpLoggingInterceptor.Logger {
            Log.i(" http : ", it)
        })

        httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BASIC
        return httpLoggingInterceptor
    }

    @Provides
    @Singleton
    fun cache(cacheFile: File): Cache {
        return Cache(cacheFile, 10 * 1000 * 1000)
    }

    @Provides
    @Singleton
    fun cacheFile(context: Context): File {
        val file = File(context.cacheDir, HTTP_CACHE)
        file.mkdirs()
        return file
    }


    @Provides
    @Singleton
    fun userAgentInterceptor(): UserAgentInterceptor {
        val userAgentInterceptor = UserAgentInterceptor(USER_AGENT)
        return userAgentInterceptor
    }
}

