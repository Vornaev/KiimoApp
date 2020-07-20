package org.kiimo.me.register.model


import com.google.gson.annotations.SerializedName

data class UserRegisterResponse(
    @SerializedName("message") val message: String,
    @SerializedName("status") val status: Int,
    @SerializedName("userID") val userID: String?
)

data class SmsUserRegisterResponse(
    @SerializedName("message") val message: String,
    @SerializedName("status") val status: Int,
    @SerializedName("userID") val userID: String?,
    val code : String
)