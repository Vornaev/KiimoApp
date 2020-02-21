package org.kiimo.me.util

import android.content.Context
import com.google.android.gms.maps.model.LatLng
import org.kiimo.me.R
import org.kiimo.me.models.Delivery
import java.util.*

object StringUtils {

    private const val ORIGIN = "origin"
    private const val DESTINATION = "destination"
    private const val MODE = "mode"
    private const val KEY = "key"
    private const val URL = "https://maps.googleapis.com/maps/api/directions/"

    fun getDirectionsUrl(
        context: Context,
        origin: LatLng,
        destination: LatLng,
        modeValue: String
    ): String {
        val originString = String.format(
            Locale.getDefault(), "%s=%f,%f",
            ORIGIN, origin.latitude, origin.longitude
        )

        val destinationString = String.format(
            Locale.getDefault(), "%s=%f,%f",
            DESTINATION, destination.latitude, destination.longitude
        )

        val mode = String.format(Locale.getDefault(), "%s=%s", MODE, modeValue)

        val key = String.format(
            Locale.getDefault(), "%s=%s",
            KEY, context.getString(R.string.google_maps_key)
        )

        val parameters = String.format(
            Locale.getDefault(), "%s&%s&%s&%s", originString, destinationString, mode, key
        )

        val output = "json"

        // Building the url to the web service
        return String.format(
            Locale.getDefault(), "%s%s?%s", URL, output, parameters
        )
    }

    fun getPackageSize(context: Context, delivery: Delivery?): String {
        delivery?.packages?.let {
            for (pack in it) {
                pack?.packageSize?.name?.let { name ->
                    return name
                }
            }
        }

        return context.getString(R.string.general_error_message)
    }
}