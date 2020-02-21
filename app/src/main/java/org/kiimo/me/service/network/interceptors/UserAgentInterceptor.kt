package org.kiimo.me.service.network.interceptors

import okhttp3.Interceptor
import okhttp3.Response



class UserAgentInterceptor(private val userAgent: String) : Interceptor {

 val headerName = "User-Agent"

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder().header(headerName, userAgent).build()
        return chain.proceed(request)
    }
}