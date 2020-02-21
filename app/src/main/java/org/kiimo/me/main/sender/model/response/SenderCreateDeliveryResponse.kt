package org.kiimo.me.main.sender.model.response


import com.google.gson.annotations.SerializedName

data class SenderCreateDeliveryResponse(
    @SerializedName("delivery")
    val delivery: Delivery = Delivery(),
    @SerializedName("previousLocations")
    val previousLocations: List<Any> = listOf()
)