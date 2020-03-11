package org.kiimo.me.main.fragments.model.deliveries


import com.google.gson.annotations.SerializedName

data class DeliveryCarrierItem(
    @SerializedName("carrierDeliveries")
    val carrierDeliveries: List<Any> = listOf(),
    @SerializedName("carrierRequests")
    val carrierRequests: List<CarrierRequest> = mutableListOf()
)