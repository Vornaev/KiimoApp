package org.kiimo.me.register.model


import com.google.gson.annotations.SerializedName

data class IsValidDelivererResponse(

    @SerializedName("isValidDeliverer")  val isValidDeliverer: Int?,

    @SerializedName("status") val status: Int = 0
)