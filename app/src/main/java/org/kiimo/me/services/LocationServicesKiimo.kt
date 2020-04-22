package org.kiimo.me.services

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.net.Uri
import android.os.Looper
import android.widget.Toast
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng
import java.util.*

object LocationServicesKiimo {

    @SuppressLint("MissingPermission")
    fun getUserDeviceLocation(context: Context, onSuccessLocation: (loc: Location) -> Unit) {

        val location =
            com.google.android.gms.location.LocationServices.getFusedLocationProviderClient(context)
                .lastLocation

        location.addOnCompleteListener {
            if (location.isSuccessful) {
                val currentLoca = location.result
                if (currentLoca?.latitude != null && currentLoca?.longitude != null) {
                    onSuccessLocation(currentLoca)
                } else {
                    Toast.makeText(
                        context,
                        "in order to use this feature turn on locationModel services",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } else {
                Toast.makeText(context, "unable to get current locationModel", Toast.LENGTH_SHORT)
                    .show()
            }
        }

    }


    fun getPeriodicLocationUpdates(
            context: Context,
            locationCallback: LocationCallback,
            interval: Long
    ) {

        val locSer = LocationServices.getFusedLocationProviderClient(context);
        val locationRequest = LocationRequest.create();
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY;
        locationRequest.interval = interval * 1000;

        locSer.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper())
    }


    fun getAddressForLocation(latitude: Double, longitude: Double, context: Context): Address? {
        try {
            val geocoder = Geocoder(context, Locale.getDefault())
            val addressList = geocoder.getFromLocation(latitude, longitude, 1)
            if (addressList == null || addressList.size == 0)
                return null
            return addressList[0]
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    fun getAddressForLocation(latLng: LatLng?, context: Context): Address? {
        if (latLng == null) {
            return null
        }

        try {
            val geocoder = Geocoder(context, Locale.getDefault())
            val addressList = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1)
            if (addressList == null || addressList.size == 0)
                return null
            return addressList[0]
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }


    fun getLocationFromAddress(city: String, country: String, context: Context): LatLng? {
        try {
            val geocoder = Geocoder(context, Locale.getDefault())
            val addressList = geocoder.getFromLocationName("$city $country", 1)
            if (addressList == null || addressList.size == 0)
                return null
            return LatLng(addressList[0].latitude, addressList[0].longitude)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }


    fun openMapsDirectionsActivity(
            context: Context,
            latitude: Double,
            longitude: Double,
            travelMode: String) {
        val uri: String = String.format(
            Locale.ENGLISH,
            "google.navigation:q=%f,%f&mode=$travelMode)",
            latitude,
            longitude
        )

        val mapIntent = Intent(Intent.ACTION_VIEW, Uri.parse(uri))
        mapIntent.setPackage("com.google.android.apps.maps")
        context.startActivity(mapIntent)
    }

    object TravelModes {
        val drive = "d"
        val bicycle = "b"
        val walking = "w"
        val scooter = "l"
    }
}