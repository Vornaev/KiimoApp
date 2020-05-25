package org.kiimo.me.main.sender.model.notifications.dropOffDeliverySender


import com.google.gson.annotations.SerializedName

data class PackageSize(
    @SerializedName("name")
    val name: String = ""
)