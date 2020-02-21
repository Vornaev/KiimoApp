package org.kiimo.me.main.menu.model

import com.google.gson.annotations.SerializedName
import org.kiimo.me.register.model.UserAddressDataRequest
import org.kiimo.me.register.model.UserRegisterDataRequest

data class UserProfileInformationResponse(
    @SerializedName("address") val address: UserAddressDataRequest,
    @SerializedName("email") val email: String,
    @SerializedName("firstName") val firstName: String,
    @SerializedName("lastName") val lastName: String,
    @SerializedName("personalID") val personalID: String = "",
    @SerializedName("photo") val photo: String = "",
    @SerializedName("user") val user: UserRegisterDataRequest,
    @SerializedName("userID") val userID: String,
    val status: String = ""
)