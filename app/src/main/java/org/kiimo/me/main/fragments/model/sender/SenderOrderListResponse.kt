package org.kiimo.me.main.fragments.model.sender


import com.google.gson.annotations.SerializedName

data class SenderOrderListResponse(
    @SerializedName("carrierDelivery")
    val carrierDelivery: CarrierDelivery = CarrierDelivery(),
    @SerializedName("createdAt")
    val createdAt: String = "",
    @SerializedName("deliveryId")
    val deliveryId: String = "",
    @SerializedName("destination")
    val destination: Destination = Destination(),
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
    val origin: Origin = Origin(),
    @SerializedName("origin_address")
    val originAddress: String = "",
    @SerializedName("packages")
    val packages: List<Package> = listOf(),
    @SerializedName("price")
    val price: Double = 0.0,
    @SerializedName("senderUserId")
    val senderUserId: String = "",
    @SerializedName("startsAt")
    val startsAt: Any? = Any(),
    @SerializedName("status")
    val status: String = "",
    @SerializedName("updatedAt")
    val updatedAt: String = ""
)