package org.kiimo.me.app

import android.content.Context
import org.kiimo.me.models.DeviceToken
import org.kiimo.me.service.network.INetworkErrorHandler

interface IBaseViewFeatures : INetworkErrorHandler {

    fun getViewContext(): Context

    fun setProgressBar(active: Boolean)

    fun getUserToken(): String

    fun getUserPhoneNumber(): String

    fun getUserPassword(): String

    fun getFcmToken(): DeviceToken

    fun trackRequestSuccess(info: String)
}