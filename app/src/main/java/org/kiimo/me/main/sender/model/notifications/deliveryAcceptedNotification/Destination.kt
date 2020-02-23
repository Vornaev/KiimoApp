package org.kiimo.me.main.sender.model.notifications.deliveryAcceptedNotification


import com.google.gson.annotations.SerializedName

data class Destination(
    @SerializedName("lat")
    val lat: Double = 0.0,
    @SerializedName("lng")
    val lng: Double = 0.0
)