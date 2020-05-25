package org.kiimo.me.util

import android.app.Activity
import android.content.Context
import android.graphics.Point
import android.util.DisplayMetrics
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment

object Utils {

    fun hideSoftKeyboard(activity: Activity?) {
        if (activity != null) {
            try {
                val inputMethodManager = activity?.getSystemService(
                        Activity.INPUT_METHOD_SERVICE) as InputMethodManager
                inputMethodManager.hideSoftInputFromWindow(
                        activity?.currentFocus!!.windowToken, 0)
            } catch (ex: Exception) {
            }
        }
    }

    fun hideSoftKeyboard(fragment: Fragment?) {
        if (fragment?.activity != null) {
            try {
                val inputMethodManager = fragment.activity?.getSystemService(
                        Activity.INPUT_METHOD_SERVICE) as InputMethodManager
                inputMethodManager.hideSoftInputFromWindow(
                        fragment.activity?.currentFocus!!.windowToken, 0)
            } catch (ex: Exception) {
            }
        }
    }

    fun List<Any>.isNotNullOrEmpty(): Boolean {
        return this.size ?: 0 > 0
    }

    fun convertDpToPixel(dp: Float, context: Context): Float {
        return dp * (context.resources.displayMetrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)
    }

    /**
     * This method converts device specific pixels to density independent pixels.
     *
     * @param px A value in px (pixels) unit. Which we need to convert into db
     * @param context Context to get resources and device specific display metrics
     * @return A float value to represent dp equivalent to px value
     */
    fun convertPixelsToDp(px: Float, context: Context): Float {
        return px / (context.resources.displayMetrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)
    }
}