package org.kiimo.me.main.sender.map

import android.content.Context
import android.graphics.Color
import android.location.Location
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.*
import org.kiimo.me.R
import org.kiimo.me.main.sender.fragment.SenderMapFragment
import org.kiimo.me.models.DestinationData
import org.kiimo.me.models.LocationModel
import org.kiimo.me.services.LocationServicesKiimo

class SenderMapFeatures(private val map: GoogleMap, private val fragment: SenderMapFragment) :
    ISenderMapFeatures {


    private var markersKiimo = MarkerKiimoSender()

    companion object {
        const val ZOOM_USER = 15f
    }

    override fun onMapReady() {
        map.setOnMapClickListener(clickListener)
        map.setOnCameraMoveListener(cameraMoveListener)
        map.setOnCameraIdleListener(onCameraIdleListener)
        map.isMyLocationEnabled = true
        map.uiSettings?.isMapToolbarEnabled = false
        map.uiSettings?.isMyLocationButtonEnabled = false
    }


    private fun getLatLngFromCamera(): LatLng {
        return LatLng(
            map.cameraPosition.target.latitude,
            map.cameraPosition.target.longitude
        )
    }

    private fun showAddressFromLoca(latLng: LatLng) {
        val address = LocationServicesKiimo.getAddressForLocation(
            latLng.latitude,
            latLng.longitude,
            getContext()
        )

        fragment.viewModel.senderProperties.pickUpAddressPoint.locationModel = LocationModel(latLng)

        if (fragment.isAdded) {
            fragment.onSenderAddressReceived(address?.getAddressLine(0) ?: "")
        }
    }


    override fun animateUserLocation(myLocation: Location) {
        val latLng = LatLng(myLocation.latitude, myLocation.longitude)
        addPickUpMarker(latLng)

        map.moveCamera(
            CameraUpdateFactory.newLatLngZoom(
                latLng, ZOOM_USER
            )
        )
    }

    override fun moveCameraToUserPos(myLocation: Location) {
        val latLng = LatLng(myLocation.latitude, myLocation.longitude)
        map.animateCamera(
            CameraUpdateFactory.newLatLngZoom(
                latLng, ZOOM_USER
            )
        )
    }


    private val clickListener = GoogleMap.OnMapClickListener { p0 ->

        if (markersKiimo.destinationToMarker != null) {
            return@OnMapClickListener
        }
        addPickUpMarker(p0!!)
        showAddressFromLoca(p0)
    }

    private val cameraMoveListener = GoogleMap.OnCameraMoveListener {
        if (markersKiimo.destinationToMarker == null) {
            moveSenderMarker(getLatLngFromCamera())
        }
    }

    private val onCameraIdleListener = GoogleMap.OnCameraIdleListener {

        if (markersKiimo.destinationToMarker != null) {
            return@OnCameraIdleListener
        }

        val latLng = getLatLngFromCamera()
        showAddressFromLoca(latLng)
        moveSenderMarker(latLng)
    }

    override fun onRouteReady(destinationData: DestinationData) {

        val points = arrayListOf<LatLng>()
        val polylineOptions = PolylineOptions()

        destinationData.routes?.apply {
            for (route in this) {
                route?.legs?.apply {
                    for (leg in this) {
                        leg?.steps?.apply {
                            for (step in this) {
                                if (step?.startLocationModel?.lat != null
                                    && step.startLocationModel.lng != null
                                    && step.endLocationModel?.lat != null
                                    && step.endLocationModel.lng != null
                                ) {
                                    points.add(
                                        LatLng(
                                            step.startLocationModel.lat,
                                            step.startLocationModel.lng
                                        )
                                    )
                                    points.add(
                                        LatLng(
                                            step.endLocationModel.lat,
                                            step.endLocationModel.lng
                                        )
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        if (points.isEmpty()) {
            return
        }
        // Adding all the points in the route to LineOptions
        polylineOptions.addAll(points)
        polylineOptions.width(10f)
        polylineOptions.color(Color.BLACK)
        polylineOptions.geodesic(true)


        markersKiimo.polyline?.let {
            it.remove()
        }

        markersKiimo.polyline = map?.addPolyline(polylineOptions)
        map.animateCamera(
            CameraUpdateFactory.newLatLngZoom(
                markersKiimo.sendFromMarker!!.position,
                11.0f
            )
        )
    }

    override fun addDestinationMarker(latLng: LatLng) {

        if (markersKiimo.destinationToMarker == null) {

            val destinationToMarker = map.addMarker(
                MarkerOptions().draggable(true).position(latLng).icon(
                    MarkerBitmapLoader.getSpecificVector(
                        getContext(),
                        R.drawable.ic_pin_dropoff_on_map
                    )
                )
            )
            markersKiimo.destinationToMarker = destinationToMarker
        } else {
            markersKiimo.destinationToMarker!!.position = latLng
        }

    }

    private fun moveSenderMarker(latLng: LatLng) {
        markersKiimo.sendFromMarker?.position = latLng
    }


    private fun getContext(): Context {
        return fragment.context!!
    }

    override fun clearMapRoute() {
        markersKiimo.polyline?.remove()
        markersKiimo.polyline = null
    }

    override fun clearMarkers() {
        markersKiimo.sendFromMarker?.remove().also { markersKiimo.sendFromMarker = null }
        markersKiimo.destinationToMarker?.remove().also { markersKiimo.destinationToMarker = null }
    }

    override fun clearMap() {
        clearMapRoute()
        clearMarkers()

        addPickUpMarker(fragment.viewModel.senderProperties.userLocation!!.toLatLng())
    }

    override fun addPickUpMarker(latLng: LatLng) {
        if (markersKiimo.sendFromMarker == null) {

            val sendFromMarker = map.addMarker(
                MarkerOptions()
                    .draggable(true)
                    .position(latLng)
                    .flat(true)
                    .icon(
                        MarkerBitmapLoader.getSpecificVector(
                            getContext(),
                            R.drawable.icon_pin_pickup_on_map
                        )
                    ).title("pick up")
            )

            markersKiimo.sendFromMarker = sendFromMarker
        } else {
            markersKiimo.sendFromMarker!!.position = latLng
        }
    }
}

data class MarkerKiimoSender(
        var sendFromMarker: Marker? = null,
        var destinationToMarker: Marker? = null,
        var polyline: Polyline? = null
)

