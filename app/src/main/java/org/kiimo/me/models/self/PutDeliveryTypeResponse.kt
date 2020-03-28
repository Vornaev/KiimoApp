package org.kiimo.me.models.self


import com.google.gson.annotations.SerializedName

data class PutDeliveryTypeResponse(
    @SerializedName("avgRating")
    val avgRating: Float = 0f,
    @SerializedName("createdAt")
    val createdAt: String = "",
    @SerializedName("currentLocation")
    val currentLocation: CurrentLocationResponse = CurrentLocationResponse(),
    @SerializedName("deliveryType")
    val deliveryType: DeliveryTypeResponse = DeliveryTypeResponse(),
    @SerializedName("deliveryTypeId")
    val deliveryTypeId: String = "",
    @SerializedName("deviceToken")
    val deviceToken: List<String> = listOf(),
    @SerializedName("online")
    val online: Boolean = false,
    @SerializedName("preferredPayment")
    val preferredPayment: String = "",
    @SerializedName("ratingCount")
    val ratingCount: Int = 0,
    @SerializedName("updatedAt")
    val updatedAt: String = "",
    @SerializedName("userId")
    val userId: String = "",
    @SerializedName("userStatusId")
    val userStatusId: String = ""
)