package org.kiimo.me.main.sender.model.notifications


import com.google.gson.annotations.SerializedName

data class Delivery(
    @SerializedName("carrier")
    val carrier: Carrier = Carrier(),
    @SerializedName("code")
    val code: String? = null,
    @SerializedName("delivery")
    val delivery: DeliveryX = DeliveryX(),
    @SerializedName("deliveryId")
    val deliveryId: String = "",
    @SerializedName("deliveryPrice")
    val deliveryPrice: DeliveryPrice = DeliveryPrice(),
    @SerializedName("deliveryType")
    val deliveryType: DeliveryType = DeliveryType(),
    @SerializedName("lastLocation")
    val lastLocation: LastLocation = LastLocation(),
    @SerializedName("status")
    val status: String = "",
    @SerializedName("userStatus")
    val userStatus: UserStatus = UserStatus()
)