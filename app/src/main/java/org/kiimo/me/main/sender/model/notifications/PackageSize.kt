package org.kiimo.me.main.sender.model.notifications


import com.google.gson.annotations.SerializedName

data class PackageSize(
    @SerializedName("name")
    val name: String = ""
)