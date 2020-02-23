package org.kiimo.me.main.sender.model.notifications.deliveryAcceptedNotification


import com.google.gson.annotations.SerializedName

data class PackageSize(
    @SerializedName("name")
    val name: String = ""
)