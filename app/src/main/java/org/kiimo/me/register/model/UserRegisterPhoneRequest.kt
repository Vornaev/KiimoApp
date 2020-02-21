package org.kiimo.me.register.model

import com.google.gson.annotations.SerializedName
import java.util.*

data class UserRegisterPhoneRequest(
    @SerializedName("user") val user: UserRegisterDataRequest,
    @SerializedName("username") val username: String,
    @SerializedName("password") val password: String = "admin ${Calendar.getInstance().time}"
)


data class UserLoginRequest(
    @SerializedName("user") val user: UserRegisterDataRequest,
    @SerializedName("username") val username: String
)

