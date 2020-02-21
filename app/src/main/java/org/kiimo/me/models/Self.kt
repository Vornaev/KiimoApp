package org.kiimo.me.models

import com.google.gson.annotations.SerializedName
import org.kiimo.me.models.self.SelfUserResponse

data class Self(
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
    val userStatus: StatusResponse? = null
)