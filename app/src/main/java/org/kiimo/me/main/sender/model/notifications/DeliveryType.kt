package org.kiimo.me.main.sender.model.notifications


import com.google.gson.annotations.SerializedName

data class DeliveryType(
    @SerializedName("type")
    val type: String = ""
)