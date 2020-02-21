package org.kiimo.me.app

import android.content.Context
import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import org.kiimo.me.BuildConfig
import org.kiimo.me.R
import org.kiimo.me.app.di.AppComponent
import org.kiimo.me.models.DeviceToken
import org.kiimo.me.util.AppConstants
import org.kiimo.me.util.DialogUtils
import org.kiimo.me.util.PreferenceUtils

abstract class BaseActivity : AppCompatActivity(), IBaseViewFeatures {


    override fun trackRequestSuccess(info: String) {
        if (BuildConfig.DEBUG)
            Toast.makeText(
                this,
                "Success PutLocation",
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
        DialogUtils.showErrorMessage(this, throwable.message)
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
}