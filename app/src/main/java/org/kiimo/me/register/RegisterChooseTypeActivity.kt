package org.kiimo.me.register

import android.content.Intent
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_register_choose_type.*
import kotlinx.android.synthetic.main.activity_register_choose_type.view.*
import kotlinx.android.synthetic.main.layout_register_deliver.view.*
import kotlinx.android.synthetic.main.layout_register_send.*
import org.kiimo.me.R
import org.kiimo.me.app.BaseActivity
import org.kiimo.me.register.model.UserCreationProperties
import org.kiimo.me.util.PreferenceUtils

class RegisterChooseTypeActivity : BaseActivity() {

    override fun getLayoutId(): Int {
        return R.layout.activity_register_choose_type
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activity_register_choose_type_send_button.setOnClickListener {
            UserCreationProperties.setAccountTypeSender()
            PreferenceUtils.saveAccountType(this,true)
            startActivity(Intent(this, RegisterSyncAccountActivity::class.java))
        }


        activity_register_choose_type_deilver_button.setOnClickListener {
            UserCreationProperties.setAccountTypeDeliverer()
            PreferenceUtils.saveAccountType(this,false)
            startActivity(Intent(this, RegisterSyncAccountActivity::class.java))
        }
    }
}