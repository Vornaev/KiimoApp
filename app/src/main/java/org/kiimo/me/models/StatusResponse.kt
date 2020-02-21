package org.kiimo.me.models

import com.google.gson.annotations.SerializedName

data class StatusResponse(
    @SerializedName("currentLocation")
    val currentLocationModel: LocationModel?,
    val userStatusId: String?,
    val userId: String?,
    val online: Boolean?,
    val deliveryTypeId: String?,
    val deviceToken: List<String?>?,
    val preferredPayment: Any?,
    val avgRating: Any?,
    val ratingCount: Any?,
    val createdAt: String?,
    val updatedAt: String?
)