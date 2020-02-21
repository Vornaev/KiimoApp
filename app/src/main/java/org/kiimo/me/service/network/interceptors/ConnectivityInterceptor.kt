package org.kiimo.me.service.network.interceptors

import android.content.Context
import okhttp3.Interceptor
import okhttp3.Response
import org.kiimo.me.R
import org.kiimo.me.service.network.utils.ConnectivityUtils
import org.kiimo.me.service.network.exception.NoConnectivityException

class ConnectivityInterceptor(private val context: Context) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        if (ConnectivityUtils.isConnected(context))
            return chain.proceed(chain.request())
        else
            throw NoConnectivityException(context.getString(R.string.general_error_no_internet))
    }
}