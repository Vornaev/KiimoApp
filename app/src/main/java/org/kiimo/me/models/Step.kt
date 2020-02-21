package org.kiimo.me.models

import com.google.android.gms.maps.model.Polyline
import com.google.gson.annotations.SerializedName

data class Step(
    val distance: TextValue?,
    val duration: TextValue?,
    @SerializedName("end_location") val endLocationModel: LocationModel?,
    @SerializedName("html_instructions") val htmlInstructions: String?,
    val polyline: Polyline?,
    @SerializedName("start_location") val startLocationModel: LocationModel?,
    @SerializedName("travel_mode") val travelMode: String?
)