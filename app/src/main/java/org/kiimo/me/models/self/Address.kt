package org.kiimo.me.models.self


import com.google.gson.annotations.SerializedName

data class SelfAddressResponse(
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