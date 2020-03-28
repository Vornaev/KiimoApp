package org.kiimo.me.main.fragments.model.sender


import com.google.gson.annotations.SerializedName

data class Package(
    @SerializedName("createdAt")
    val createdAt: String = "",
    @SerializedName("deliveryId")
    val deliveryId: String = "",
    @SerializedName("description")
    val description: String = "",
    @SerializedName("images")
    val images: List<String> = listOf(),
    @SerializedName("packageId")
    val packageId: String = "",
    @SerializedName("packageSize")
    val packageSize: PackageSize = PackageSize(),
    @SerializedName("packageSizeId")
    val packageSizeId: String = "",
    @SerializedName("title")
    val title: String = "",
    @SerializedName("updatedAt")
    val updatedAt: String = "",
    @SerializedName("videos")
    val videos: Any? = Any()
)