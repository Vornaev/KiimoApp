package org.kiimo.me.main.sender.model.request


import com.google.gson.annotations.SerializedName

data class Packages(
    @SerializedName("description")
    var description: String = "test description",
    @SerializedName("images")
    val images: List<Any> = listOf(),
    @SerializedName("packageSizeId")
    var packageSizeId: String = "38983d4e-a170-4c4e-a32a-f20fbabbc31e",
    @SerializedName("phoneNumber")
    var phoneNumber: String = "",
    @SerializedName("title")
    var title: String = ""
)