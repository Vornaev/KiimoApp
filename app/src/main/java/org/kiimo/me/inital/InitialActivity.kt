package org.kiimo.me.inital

import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.Observer
import kotlinx.android.synthetic.main.activity_initial.*
import org.kiimo.me.R
import org.kiimo.me.app.BaseActivity
import org.kiimo.me.main.MainActivity
import org.kiimo.me.register.BaseRegistrationServicesActivity
import org.kiimo.me.register.RegisterChooseTypeActivity


class InitialActivity : BaseRegistrationServicesActivity() {


    override fun getLayoutId(): Int {
        return R.layout.activity_initial
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setButtonListener()

    }

    private fun setButtonListener() {
        activityInitialContinueButton.setOnClickListener {
            val intent = Intent(this, RegisterChooseTypeActivity::class.java)
            startActivity(intent)
        }
    }
}
