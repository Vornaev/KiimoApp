package org.kiimo.me.register.model


import com.google.gson.annotations.SerializedName

data class UserProfileInformationRequest(
    @SerializedName("address") var address: UserAddressDataRequest,
    @SerializedName("email") var email: String,
    @SerializedName("firstName") var firstName: String,
    @SerializedName("lastName") var lastName: String,
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

data class UserProfileFragmentUpdateRequest(
        @SerializedName("email") var email: String,
        @SerializedName("firstName") var firstName: String,
        @SerializedName("lastName") var lastName: String,
        @SerializedName("userID") val userID: String,
        @SerializedName("user") var user: UserRegisterDataRequest
)
