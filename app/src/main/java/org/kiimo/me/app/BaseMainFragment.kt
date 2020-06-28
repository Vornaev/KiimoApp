package org.kiimo.me.app

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import androidx.core.content.ContextCompat
import io.reactivex.disposables.CompositeDisposable
import org.kiimo.me.main.MainActivity
import org.kiimo.me.main.fragments.DeliveryMapFragment
import org.kiimo.me.main.menu.KiimoMainNavigationActivity
import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.*


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

    protected fun attachListenerToField(field: EditText, listener: () -> Boolean) {
        field.addTextChangedListener(
            object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                    if (s!=null && s.length > 1) {
                        listener()
                    }
                }

                override fun beforeTextChanged(
                    s: CharSequence?, start: Int, count: Int, after: Int
                ) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                }

            }
        )
    }

    protected fun loadCurrency(code: String, formattedNumber: Double): String? {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            val required = Currency.getAvailableCurrencies()
            val expted = required.firstOrNull { it.numericCode == code.toInt() }

            val numberFormat = NumberFormat.getNumberInstance()
            numberFormat.currency = expted
            return "${numberFormat.format(formattedNumber)} ${numberFormat.currency.displayName}"

        } else {
            val decimalFormat = DecimalFormat("###")
            return "${decimalFormat.format(formattedNumber)}  MKD"
        }
    }

    override fun onDestroyView() {
        compositeDisposable.clear()
        super.onDestroyView()
    }
}