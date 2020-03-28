package org.kiimo.me.main.fragments.model.sender


import com.google.gson.annotations.SerializedName

data class CarrierDelivery(
    @SerializedName("carrierDeliveryId")
    val carrierDeliveryId: String = "",
    @SerializedName("carrierRequestId")
    val carrierRequestId: String = "",
    @SerializedName("carrierUserId")
    val carrierUserId: String = "",
    @SerializedName("code")
    val code: String = "",
    @SerializedName("createdAt")
    val createdAt: String = "",
    @SerializedName("deliveredAt")
    val deliveredAt: String = "",
    @SerializedName("delivery")
    val delivery: Delivery = Delivery(),
    @SerializedName("deliveryId")
    val deliveryId: String = "",
    @SerializedName("deliveryType")
    val deliveryType: DeliveryType = DeliveryType(),
    @SerializedName("deliveryTypeId")
    val deliveryTypeId: String = "",
    @SerializedName("lastLocation")
    val lastLocation: LastLocation = LastLocation(),
    @SerializedName("pickUpImage")
    val pickUpImage: Any? = Any(),
    @SerializedName("pickedupAt")
    val pickedupAt: String = "",
    @SerializedName("signature")
    val signature: String = "",
    @SerializedName("status")
    val status: String = "",
    @SerializedName("updatedAt")
    val updatedAt: String = ""
)