package org.kiimo.me.models.self


import com.google.gson.annotations.SerializedName

data class CurrentLocationResponse(
    @SerializedName("lat")
    val lat: Double = 0.0,
    @SerializedName("lng")
    val lng: Double = 0.0
)