package org.kiimo.me.service.network

interface INetworkErrorHandler {

    fun handleApiError(throwable: Throwable)
}