package org.kiimo.me.register

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.Observer
import org.kiimo.me.R
import org.kiimo.me.inital.InitialActivity
import org.kiimo.me.main.MainActivity
import org.kiimo.me.main.sender.SenderKiimoActivity
import org.kiimo.me.util.AppConstants
import org.kiimo.me.util.DialogUtils
import org.kiimo.me.util.PreferenceUtils
import timber.log.Timber


class WelcomeActivity : BaseRegistrationServicesActivity() {

    override fun getLayoutId(): Int {
        return R.layout.activity_welcome
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.userLoginLiveData.observe(this, Observer {

            if (it.status == 200) {
                if (it.existUser && !it.userID.isNullOrBlank()) {

                    val isSender = PreferenceUtils.getAccountTypeIsSender(this)

                    val newintent = Intent(
                        this,
                        if (isSender) SenderKiimoActivity::class.java else MainActivity::class.java
                    )

                    val message = intent?.getStringExtra("payload")
                    newintent.putExtra(AppConstants.FIREBASE_PAYLOAD, message)
                    Log.i("notification", message ?: "")
                    Timber.log( 1,"notification data",message ?: "")

                    newintent.flags =
                        Intent.FLAG_ACTIVITY_CLEAR_TOP and Intent.FLAG_ACTIVITY_NEW_TASK
                    startActivity(newintent)
                } else {
                    startInital()
                }
            }
        })

        if (getUserPhoneNumber().isNotEmpty()) {
            viewModel.userLogin()
        } else {
            startInital()
        }
    }

    fun startInital() {
        startActivity(Intent(this, InitialActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK and Intent.FLAG_ACTIVITY_CLEAR_TASK
        })
    }
}