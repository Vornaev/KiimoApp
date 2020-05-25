package org.kiimo.me.main.sender.map

import android.location.Location
import com.google.android.gms.maps.model.LatLng
import org.kiimo.me.models.DestinationData

interface ISenderMapFeatures {

    fun animateUserLocation(loc :Location)

    fun onMapReady()

    fun addPickUpMarker(latLng: LatLng)

    fun onRouteReady(destinationData: DestinationData)

    fun addDestinationMarker(latLng: LatLng)

    fun clearMapRoute()

    fun clearMarkers()

    fun clearMap()

    fun moveCameraToUserPos(myLocation: Location)

    fun onPermissionGranted()
}
