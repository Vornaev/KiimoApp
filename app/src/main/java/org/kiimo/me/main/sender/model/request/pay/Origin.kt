package org.kiimo.me.main.sender.model.request.pay


import com.google.gson.annotations.SerializedName

data class Origin(
    @SerializedName("lat")
    val lat: Double = 0.0,
    @SerializedName("lng")
    val lng: Double = 0.0
)