package org.kiimo.me.main.sender.model.request.pay


import com.google.gson.annotations.SerializedName

data class Carrier(
    @SerializedName("firstName")
    val firstName: String = "",
    @SerializedName("lastName")
    val lastName: String = "",
    @SerializedName("photo")
    var photo: String = ""
)