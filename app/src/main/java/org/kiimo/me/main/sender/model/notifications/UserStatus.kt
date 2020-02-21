package org.kiimo.me.main.sender.model.notifications


import com.google.gson.annotations.SerializedName

data class UserStatus(
    @SerializedName("avgRating")
    val avgRating: Any? = Any()
)