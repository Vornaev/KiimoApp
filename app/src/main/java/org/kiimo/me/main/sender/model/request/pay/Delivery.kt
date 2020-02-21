package org.kiimo.me.main.sender.model.request.pay


import com.google.gson.annotations.SerializedName

data class Delivery(
    @SerializedName("destination")
    val destination: Destination = Destination(),
    @SerializedName("destination_address")
    val destinationAddress: String = "",
    @SerializedName("origin")
    val origin: Origin = Origin(),
    @SerializedName("origin_address")
    val originAddress: String = "",
    @SerializedName("packages")
    val packages: List<Package> = listOf()
)