package org.kiimo.me.main.sender.model.notifications.dropOffDeliverySender


import com.google.gson.annotations.SerializedName

data class FcmResponseOnDropOffDelivery(
    @SerializedName("carrier")
    val carrier: Carrier = Carrier(),
    @SerializedName("carrierUserId")
    val carrierUserId: String = "",
    @SerializedName("code")
    val code: String = "",
    @SerializedName("delivery")
    val delivery: Delivery = Delivery(),
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