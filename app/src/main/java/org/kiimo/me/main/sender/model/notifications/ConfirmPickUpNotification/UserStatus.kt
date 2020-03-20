package org.kiimo.me.main.sender.model.notifications.ConfirmPickUpNotification


import com.google.gson.annotations.SerializedName

data class UserStatus(
    @SerializedName("avgRating")
    val avgRating: Float = 0f
)