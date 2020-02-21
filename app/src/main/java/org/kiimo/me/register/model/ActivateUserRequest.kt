package org.kiimo.me.register.model

import com.google.gson.annotations.SerializedName

data class ActivateUserRequest(

    @SerializedName("user") val user: UserRegisterDataRequest,
    @SerializedName("userID") val userId: String
)
