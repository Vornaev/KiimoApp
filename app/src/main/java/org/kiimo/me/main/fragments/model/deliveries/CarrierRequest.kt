package org.kiimo.me.main.fragments.model.deliveries


import com.google.gson.annotations.SerializedName

data class CarrierRequest(
    @SerializedName("carrierRequestId")
    val carrierRequestId: String = "",
    @SerializedName("carrierUserId")
    val carrierUserId: String = "",
    @SerializedName("createdAt")
    val createdAt: String = "",
    @SerializedName("deliveryId")
    val deliveryId: String = "",
    @SerializedName("status")
    val status: String = "",
    @SerializedName("updatedAt")
    val updatedAt: String = ""
)