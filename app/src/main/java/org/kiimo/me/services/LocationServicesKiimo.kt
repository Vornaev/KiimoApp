package org.kiimo.me.services

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.IntentSender
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.net.Uri
import android.os.Looper
import android.provider.Settings
import android.widget.Toast
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.content.ContextCompat.startActivity
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.tasks.Task
import org.kiimo.me.app.BaseActivity
import org.kiimo.me.util.DialogUtils
import java.util.*

object LocationServicesKiimo {


    data class LocEvent(val changed: Boolean)

    const val LOCATION_SETTINGS_CODE = 5021

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
//                    if (hasGPSenabled(context)) {
                    Toast.makeText(
                        context,
                        "In order to use this feature turn on Location Service",
                        Toast.LENGTH_LONG
                    ).show()
//                    }
//                    //}
                }
            } else {
                Toast.makeText(context, "unable to get current location", Toast.LENGTH_SHORT)
                    .show()
            }
        }

    }

    fun hasGPSenabled(context: Context): Boolean {
        val manager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager;

        var hasLocationService = true

        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            hasLocationService = false
            buildAlertMessageNoGps(context)
        }

        return hasLocationService
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


    public fun buildAlertMessageNoGps(context: Context) {
        val builder = androidx.appcompat.app.AlertDialog.Builder(context)

        builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
            .setCancelable(false)
            .setPositiveButton("Yes") { dialog, id ->
                (context as Activity).startActivityForResult(
                    Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS),
                    LOCATION_SETTINGS_CODE
                )
            }
            .setNegativeButton("No", object : DialogInterface.OnClickListener {
                override fun onClick(dialog: DialogInterface, id: Int) {
                    dialog.cancel()
                }
            })
        val alert: androidx.appcompat.app.AlertDialog = builder.create()
        alert.show()
    }


    //lastest API
    fun checkForLocationServiceEnabled(context: BaseActivity) {
        val locBuilder = LocationSettingsRequest.Builder()
        val locClient = LocationServices.getSettingsClient(context)
        val task: Task<LocationSettingsResponse> =
            locClient.checkLocationSettings(locBuilder.build())

        task.addOnSuccessListener { locationSettingsResponse ->
            // All location settings are satisfied. The client can initialize
            // location requests here.
            // ...
        }

        task.addOnFailureListener { exception ->
            if (exception is ResolvableApiException) {
                // Location settings are not satisfied, but this can be fixed
                // by showing the user a dialog.
                try {
                    // Show the dialog by calling startResolutionForResult(),
                    // and check the result in onActivityResult().
                    val REQUEST_CHECK_SETTINGS = 1930
                    exception.startResolutionForResult(
                        context,
                        REQUEST_CHECK_SETTINGS
                    )
                } catch (sendEx: IntentSender.SendIntentException) {
                    // Ignore the error.
                }
            }
        }
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