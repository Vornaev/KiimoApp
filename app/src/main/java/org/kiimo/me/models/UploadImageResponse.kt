package org.kiimo.me.models


import com.google.gson.annotations.SerializedName

data class UploadImageResponse(
    @SerializedName("imageUrl")
    val imageUrl: String = "",
    @SerializedName("success")
    val success: Boolean = false
)