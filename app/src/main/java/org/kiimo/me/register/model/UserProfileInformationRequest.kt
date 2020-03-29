package org.kiimo.me.register.model


import com.google.gson.annotations.SerializedName

data class UserProfileInformationRequest(
    @SerializedName("address") var address: UserAddressDataRequest,
    @SerializedName("email") val email: String,
    @SerializedName("firstName") val firstName: String,
    @SerializedName("lastName") val lastName: String,
    @SerializedName("personalID") var personalID: String = "",
    @SerializedName("photo") val photo: String = "",
    @SerializedName("user") var user: UserRegisterDataRequest,
    @SerializedName("userID") val userID: String
)


data class UserProfileUpdatePhotoRequest(
    @SerializedName("photo") val photo: String,
    @SerializedName("userID") val userID: String,
    @SerializedName("user") var user: UserRegisterDataRequest
)
