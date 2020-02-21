package org.kiimo.me.register.model


import com.google.gson.annotations.SerializedName

data class SmsValidationRequest(
    @SerializedName("smsCode")
    val smsCode: String = "",

    @SerializedName("username")
    val username: String = ""
)