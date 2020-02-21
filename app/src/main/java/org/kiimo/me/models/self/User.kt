package org.kiimo.me.models.self


import com.google.gson.annotations.SerializedName

data class SelfUserResponse(
    @SerializedName("address")
    val address: SelfAddressResponse = SelfAddressResponse(),
    @SerializedName("email")
    val email: String = "",
    @SerializedName("firstName")
    val firstName: String = "",
    @SerializedName("lastName")
    val lastName: String = "",
    @SerializedName("personalID")
    val personalID: String = "",
    @SerializedName("photo")
    val photo: String = "",
    @SerializedName("status")
    val status: Int = 0,
    @SerializedName("userID")
    val userID: String = "",
    @SerializedName("username")
    val username: String = ""
)