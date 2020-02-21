package org.kiimo.me.main.sender.model.response


import com.google.gson.annotations.SerializedName

data class Package(
    @SerializedName("createdAt")
    val createdAt: String = "",
    @SerializedName("deliveryId")
    val deliveryId: String = "",
    @SerializedName("description")
    val description: String = "",
    @SerializedName("images")
    val images: List<Any> = listOf(),
    @SerializedName("packageId")
    val packageId: String = "",
    @SerializedName("packageSizeId")
    val packageSizeId: String = "",
    @SerializedName("phoneNumber")
    val phoneNumber: Any? = Any(),
    @SerializedName("title")
    val title: String = "",
    @SerializedName("updatedAt")
    val updatedAt: String = "",
    @SerializedName("videos")
    val videos: Any? = Any()
)