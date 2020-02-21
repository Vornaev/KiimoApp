package org.kiimo.me.main.sender.model.notifications.ConfirmPickUpNotification


import com.google.gson.annotations.SerializedName

data class PackageSize(
    @SerializedName("name")
    val name: String = ""
)