package org.kiimo.me.models

import com.google.gson.annotations.SerializedName

data class Leg(
    val distance: TextValue?,
    val duration: TextValue?,
    @SerializedName("end_address") val endAddress: String?,
    @SerializedName("end_location") val endLocationModel: LocationModel?,
    @SerializedName("start_address") val startAddress: String?,
    @SerializedName("start_location") val startLocationModel: LocationModel?,
    val steps: List<Step?>?
)