package org.kiimo.me.register.model


import com.google.gson.annotations.SerializedName

data class UserAddressDataRequest(
    @SerializedName("countryCode")
    val countryCode: String = "",
    @SerializedName("houseNumber")
    val houseNumber: String = "",
    @SerializedName("place")
    val place: String = "",
    @SerializedName("street")
    val street: String = "",
    @SerializedName("zip")
    val zip: String = ""
)