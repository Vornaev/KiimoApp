package org.kiimo.me.main.sender.model.notifications.dropOffDeliverySender


import com.google.gson.annotations.SerializedName

data class DeliveryPrice(
    @SerializedName("basePrice")
    val basePrice: Int = 0,
    @SerializedName("bruttoAmount")
    val bruttoAmount: String = "",
    @SerializedName("currencyId")
    val currencyId: String = "",
    @SerializedName("kilometers")
    val kilometers: Double = 0.0,
    @SerializedName("minutes")
    val minutes: Double = 0.0,
    @SerializedName("nettoAmount")
    val nettoAmount: String = "",
    @SerializedName("pricePerKm")
    val pricePerKm: Int = 0,
    @SerializedName("pricePerMin")
    val pricePerMin: Double = 0.0
)