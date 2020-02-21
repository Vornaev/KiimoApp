package org.kiimo.me.models.self


import com.google.gson.annotations.SerializedName

data class SelfResponse(
    @SerializedName("balance")
    val balance: Any? = Any(),
    @SerializedName("card")
    val card: Any? = Any(),
    @SerializedName("currentCarrierDelivery")
    val currentCarrierDelivery: Any? = Any(),
    @SerializedName("previousLocations")
    val previousLocations: List<Any> = listOf(),
    @SerializedName("success")
    val success: Boolean = false,
    @SerializedName("user")
    val user: SelfUserResponse = SelfUserResponse(),
    @SerializedName("userStatus")
    val userStatus: Any? = Any()
)