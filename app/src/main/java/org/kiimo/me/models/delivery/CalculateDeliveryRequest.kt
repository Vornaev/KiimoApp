package org.kiimo.me.models.delivery


import com.google.gson.annotations.SerializedName
import org.kiimo.me.models.LocationModel

data class CalculateDeliveryRequest(
    @SerializedName("destination")
    val destination: LocationModel? = null,
    @SerializedName("origin")
    val origin: LocationModel? = null
)