package org.kiimo.me.main.sender.model.notifications


import com.google.gson.annotations.SerializedName

data class FirebaseSenderResponse(
    @SerializedName("delivery")
    val delivery: Delivery = Delivery(),
    @SerializedName("type")
    val type: String = ""
)