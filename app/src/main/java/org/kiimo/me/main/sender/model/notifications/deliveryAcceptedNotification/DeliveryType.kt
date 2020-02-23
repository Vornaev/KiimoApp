package org.kiimo.me.main.sender.model.notifications.deliveryAcceptedNotification


import com.google.gson.annotations.SerializedName

data class DeliveryType(
    @SerializedName("type")
    val type: String = ""
)