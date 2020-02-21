package org.kiimo.me.models.delivery


import com.google.gson.annotations.SerializedName

data class DropOffDeliveryResponse(
    @SerializedName("message")
    val message: String = "",
    @SerializedName("success")
    val success: Boolean = false
)