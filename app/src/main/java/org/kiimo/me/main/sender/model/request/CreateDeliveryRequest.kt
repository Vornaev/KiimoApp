package org.kiimo.me.main.sender.model.request


import com.google.gson.annotations.SerializedName
import org.kiimo.me.models.LocationModel

data class CreateDeliveryRequest(
    @SerializedName("destination")
    val destination: LocationModel? = null,
    @SerializedName("destination_address")
    val destinationAddress: String = "",
    @SerializedName("distanceByBike")
    val distanceByBike: Int = 0,
    @SerializedName("distanceByCar")
    val distanceByCar: Int = 0,
    @SerializedName("distanceByFoot")
    val distanceByFoot: Int = 0,
    @SerializedName("distanceByKickScooter")
    val distanceByKickScooter: Int = 0,
    @SerializedName("origin")
    val origin: LocationModel? = null,
    @SerializedName("origin_address")
    val originAddress: String = "",
    @SerializedName("packages")
    val packages: Packages = Packages()
)