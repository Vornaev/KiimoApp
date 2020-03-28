package org.kiimo.me.register.model

import com.google.gson.annotations.SerializedName

data class UserSmsCodeRequest(

    @SerializedName("username") val username: String
)