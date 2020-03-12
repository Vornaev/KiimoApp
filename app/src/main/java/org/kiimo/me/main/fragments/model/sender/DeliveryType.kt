package org.kiimo.me.main.fragments.model.sender


import com.google.gson.annotations.SerializedName

data class DeliveryType(
    @SerializedName("createdAt")
    val createdAt: String = "",
    @SerializedName("deliveryTypeId")
    val deliveryTypeId: String = "",
    @SerializedName("picture")
    val picture: String = "",
    @SerializedName("price")
    val price: Int = 0,
    @SerializedName("type")
    val type: String = "",
    @SerializedName("updatedAt")
    val updatedAt: String = "",
    @SerializedName("weight")
    val weight: Int = 0
)