package org.kiimo.me.main.sender.model.notifications.ConfirmPickUpNotification


import com.google.gson.annotations.SerializedName

data class Origin(
    @SerializedName("lat")
    val lat: Double = 0.0,
    @SerializedName("lng")
    val lng: Double = 0.0
)