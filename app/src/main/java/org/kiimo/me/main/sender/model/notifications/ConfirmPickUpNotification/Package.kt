package org.kiimo.me.main.sender.model.notifications.ConfirmPickUpNotification


import com.google.gson.annotations.SerializedName

data class Package(
    @SerializedName("description")
    val description: String = "",
    @SerializedName("images")
    val images: List<Any> = listOf(),
    @SerializedName("packageSize")
    val packageSize: PackageSize = PackageSize()
)