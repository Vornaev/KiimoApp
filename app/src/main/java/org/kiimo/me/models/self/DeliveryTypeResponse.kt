package org.kiimo.me.models.self


import com.google.gson.annotations.SerializedName

data class DeliveryTypeResponse(
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