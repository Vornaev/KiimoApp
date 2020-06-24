package org.kiimo.me.app

import android.Manifest
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import io.reactivex.disposables.CompositeDisposable
import org.kiimo.me.main.MainActivity
import org.kiimo.me.main.fragments.DeliveryMapFragment
import org.kiimo.me.main.menu.KiimoMainNavigationActivity

abstract class BaseMainFragment : BaseFragment() {

    val compositeDisposable = CompositeDisposable()

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
                    DeliveryMapFragment.MY_LOCATION_REQUEST_CODE
                )
            }
        }
    }

    protected fun hasLocationPermission(): Boolean {
        return (ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED)
    }

    override fun onRequestPermissionsResult(
            requestCode: Int,
            permissions: Array<out String>,
            grantResults: IntArray
    ) {
        if (requestCode == DeliveryMapFragment.MY_LOCATION_REQUEST_CODE) {
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

    override fun onDestroyView() {
        compositeDisposable.clear()
        super.onDestroyView()
    }
}