package org.kiimo.me.models

import com.google.gson.annotations.SerializedName

data class Delivery(
    val origin: LocationModel?,
    val destination: LocationModel?,
    val deliveryId: String?,
    val distanceByFoot: Double?,
    val distanceByKickScooter: Double?,
    val distanceByBike: Double?,
    val distanceByCar: Double?,
    val startsAt: Any?,
    val senderUserId: String?,
    @SerializedName("origin_address") val originAddress: String?,
    @SerializedName("destination_address") val destinationAddress: String?,
    val status: String?,
    val createdAt: String?,
    val updatedAt: String?,
    val packages: List<Package?>?,
    val deliveryPrice: DeliveryPrice?
)