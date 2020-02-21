package org.kiimo.me.service.network.exception

import java.io.IOException

class NoConnectivityException(private val errorMessage: String) : IOException() {

    override fun getLocalizedMessage(): String = errorMessage
}