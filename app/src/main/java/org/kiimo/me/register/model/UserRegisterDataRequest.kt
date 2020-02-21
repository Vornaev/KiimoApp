package org.kiimo.me.register.model

import com.google.gson.annotations.SerializedName

data class UserRegisterDataRequest(
    @SerializedName("userName") val usernameApi: String = "administrator",
    @SerializedName("pass") val passwordApi: String = "administrator"
)