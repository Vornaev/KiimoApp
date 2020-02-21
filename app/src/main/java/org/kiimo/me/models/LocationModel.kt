package org.kiimo.me.models

import android.location.Location
import com.google.android.gms.maps.model.LatLng

data class LocationModel(val lat: Double?, val lng: Double?) {

    constructor(latLng: LatLng) : this(latLng.latitude, latLng.longitude) {

    }

    fun toLatLng(): LatLng {
        return LatLng(lat ?: 0.0, lng ?: 0.0)
    }

    fun toGoogleLoc(name: String): Location {
        return Location(name).apply {
            this.latitude = lat!!
            this.longitude = lng!!
        }
    }
}