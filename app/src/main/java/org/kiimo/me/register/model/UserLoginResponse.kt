package org.kiimo.me.register.model


import com.google.gson.annotations.SerializedName

data class UserLoginResponse(
    @SerializedName("existUser")
    val existUser: Boolean = false,
    @SerializedName("sessionID")
    val sessionID: String = "",
    @SerializedName("status")
    val status: Int = 0,
    @SerializedName("userID")
    val userID: String? = ""
)