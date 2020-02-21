package org.kiimo.me.models

import com.google.gson.annotations.SerializedName

data class LocationRequest(
    @SerializedName("location")
    val locationModel: LocationModel
)