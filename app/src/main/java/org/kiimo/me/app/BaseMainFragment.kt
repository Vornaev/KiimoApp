package org.kiimo.me.app

import android.Manifest
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import org.kiimo.me.main.MainActivity
import org.kiimo.me.main.fragments.MapFragment
import org.kiimo.me.main.menu.KiimoMainNavigationActivity

abstract class BaseMainFragment : BaseFragment() {


    fun getMainActivity(): MainActivity? {
        return this.activity as MainActivity?
    }

    fun getNavigationActivity(): KiimoMainNavigationActivity {
        return this.activity as KiimoMainNavigationActivity
    }

    fun mainDeliveryViewModel() = getNavigationActivity()!!.viewModel

    protected fun requestAccessFineLocationPermission() {
        val activity = activity
        activity?.let {
            if (ContextCompat.checkSelfPermission(
                    activity,
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
                == PackageManager.PERMISSION_GRANTED
            ) {
                onPermissionLocationEnabled()
            } else {
                // Permission is not granted
                requestPermissions(
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    MapFragment.MY_LOCATION_REQUEST_CODE
                )
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == MapFragment.MY_LOCATION_REQUEST_CODE) {
            if (permissions.size == 1 &&
                permissions[0] == Manifest.permission.ACCESS_FINE_LOCATION &&
                grantResults[0] == PackageManager.PERMISSION_GRANTED
            ) {
                onPermissionLocationEnabled()
            }
        }
    }

    protected open fun onPermissionLocationEnabled() {

    }
}