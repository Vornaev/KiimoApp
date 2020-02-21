package org.kiimo.me.main.sender.model.request.pay


import com.google.gson.annotations.SerializedName

data class PackageSize(
    @SerializedName("name")
    val name: String = ""
)