package org.kiimo.me.app

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.crashlytics.android.Crashlytics
import com.google.firebase.analytics.FirebaseAnalytics
import org.kiimo.me.BuildConfig
import org.kiimo.me.R
import org.kiimo.me.app.di.AppComponent
import org.kiimo.me.models.DeviceToken
import org.kiimo.me.services.LocationServicesKiimo
import org.kiimo.me.services.LocationServicesKiimo.LOCATION_SETTINGS_CODE
import org.kiimo.me.util.*

abstract class BaseActivity : AppCompatActivity(), IBaseViewFeatures, IMediaManagerImages {


    override fun trackRequestSuccess(info: String) {
        if (BuildConfig.DEBUG)
            Toast.makeText(
                this,
                info,
                Toast.LENGTH_SHORT
            ).show()
    }


    abstract fun getLayoutId(): Int

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayoutId())
    }

    protected fun getAppComponent(): AppComponent {
        return (this.application as App).getAppComponent()
    }

    override fun handleApiError(throwable: Throwable) {
        //DialogUtils.showErrorMessage(this, throwable.message)
        Crashlytics.logException(throwable)
    }

    override fun getFcmToken(): DeviceToken {
        return DeviceToken(PreferenceUtils.getUserFirebaseToken(this))
    }

    override fun getViewContext(): Context {
        return this
    }

    override fun setProgressBar(active: Boolean) {}

    fun EditText.textValue(): String = this.text.toString()

    fun addFragment(fragment: Fragment) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()

//        fragmentTransaction.setCustomAnimations(
//            R.anim.slide_in_right,
//            R.anim.slide_out_left,
//            R.anim.slide_in_right,
//            R.anim.slide_out_left
//        )

        fragmentTransaction.setCustomAnimations(
            R.anim.slide_in_up,
            R.anim.slide_out_down,
            R.anim.slide_in_up,
            R.anim.slide_out_down
        )

        fragmentTransaction.add(R.id.main_fragment_layout, fragment)
        fragmentTransaction.addToBackStack(fragment::class.java.name)
        fragmentTransaction.commit()
    }

    fun replaceFragment(fragment: Fragment) {
        val fragmentManager = supportFragmentManager
        fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
        val fragmentTransaction = fragmentManager.beginTransaction()

        fragmentTransaction.replace(R.id.main_fragment_layout, fragment)
        fragmentTransaction.commit()
        fragmentTransaction.addToBackStack(AppConstants.MAP_ROOT_FRAGMENT)
    }


    fun <T : BaseFragment> openMenuFragment(fragmentInstance: Class<T>, name: String) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()

        var frag = fragmentManager.findFragmentByTag(name)
        if (frag == null) {
            frag = fragmentInstance.newInstance()
            fragmentTransaction.add(R.id.main_fragment_layout, frag!!, name)
        } else {
            fragmentTransaction.replace(R.id.main_fragment_layout, frag, name)
        }

        fragmentTransaction.addToBackStack(name)
        fragmentTransaction.commit()
    }

    override fun getUserToken(): String {
        return PreferenceUtils.getUserToken(this)
    }

    override fun getUserPassword(): String {
        return PreferenceUtils.getUserPassord(this)
    }

    override fun getUserPhoneNumber(): String {
        return PreferenceUtils.getUserPhoneNumber(this)
    }

    override fun getMediaContext(): Context {
        return this
    }

    override fun startMediaActivity(createChooser: Intent?, requestImagePick: Int) {
        startActivityForResult(createChooser, requestImagePick)
    }

    override fun getMediaActivity(): Activity {
        return this
    }


    private val MY_PERMISSION_CAMERA = 301
    fun hasCameraPermission(): Boolean {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.CAMERA
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return false
        }
        return true
    }

    fun requestCameraPermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.CAMERA),
            MY_PERMISSION_CAMERA
        )
    }

    override fun onRequestPermissionsResult(
            requestCode: Int,
            permissions: Array<out String>,
            grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode) {
            MY_PERMISSION_CAMERA -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    MediaManager.showMediaOptionsDialog(this)
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == LocationServicesKiimo.LOCATION_SETTINGS_CODE) {
            RxBus.publish(LocationServicesKiimo.LocEvent(true))
        }
    }
}