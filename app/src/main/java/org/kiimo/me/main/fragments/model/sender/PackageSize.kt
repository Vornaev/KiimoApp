package org.kiimo.me.main.fragments.model.sender


import com.google.gson.annotations.SerializedName

data class PackageSize(
    @SerializedName("createdAt")
    val createdAt: String = "",
    @SerializedName("description")
    val description: String = "",
    @SerializedName("name")
    val name: String = "",
    @SerializedName("packageSizeId")
    val packageSizeId: String = "",
    @SerializedName("price")
    val price: Int = 0,
    @SerializedName("size")
    val size: Int = 0,
    @SerializedName("updatedAt")
    val updatedAt: String = ""
)