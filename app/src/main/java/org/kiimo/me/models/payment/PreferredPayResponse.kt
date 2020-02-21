package org.kiimo.me.models.payment


import com.google.gson.annotations.SerializedName

data class PreferredPayResponse(
    @SerializedName("avgRating")
    val avgRating: Any? = Any(),
    @SerializedName("createdAt")
    val createdAt: String = "",
    @SerializedName("currentLocation")
    val currentLocation: CurrentLocation = CurrentLocation(),
    @SerializedName("deliveryTypeId")
    val deliveryTypeId: Any? = Any(),
    @SerializedName("deviceToken")
    val deviceToken: List<Any> = listOf(),
    @SerializedName("online")
    val online: Boolean = false,
    @SerializedName("preferredPayment")
    val preferredPayment: String = "",
    @SerializedName("ratingCount")
    val ratingCount: Any? = Any(),
    @SerializedName("updatedAt")
    val updatedAt: String = "",
    @SerializedName("userId")
    val userId: String = "",
    @SerializedName("userStatusId")
    val userStatusId: String = ""
)