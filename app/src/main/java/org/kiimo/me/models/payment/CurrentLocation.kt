package org.kiimo.me.models.payment


import com.google.gson.annotations.SerializedName

data class CurrentLocation(
    @SerializedName("lat")
    val lat: Double = 0.0,
    @SerializedName("lng")
    val lng: Double = 0.0
)