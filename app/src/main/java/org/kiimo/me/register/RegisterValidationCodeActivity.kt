package org.kiimo.me.register

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import androidx.lifecycle.Observer
import kotlinx.android.synthetic.main.activity_register_validation_code.*
import org.kiimo.me.R
import org.kiimo.me.main.MainActivity
import org.kiimo.me.main.sender.SenderKiimoActivity
import org.kiimo.me.register.model.UserCreationProperties
import org.kiimo.me.util.PreferenceUtils

class RegisterValidationCodeActivity : BaseRegistrationServicesActivity() {

    companion object {
        val SMS_KEY_CODE = "SMSCODE"
    }

    var smsCode = ""

    override fun getLayoutId(): Int {
        return R.layout.activity_register_validation_code
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        smsCode = intent.getStringExtra(SMS_KEY_CODE)

        setEditTextListner()


        viewModel.smsValidationCodeLiveData.observe(
            this,
            Observer {
                if (UserCreationProperties.isAlreadyUser()) {
                    viewModel.userLogin()
                } else {
                    openProfileRegisterInformation()
                }
            }
        )

        viewModel.userLoginLiveData.observe(this, Observer {
            if (it.existUser && !it.userID.isNullOrBlank()) {
                if (PreferenceUtils.getAccountTypeIsSender(this)) {
                    startActivity(Intent(this, SenderKiimoActivity::class.java))
                } else {
                    startActivity(Intent(this, MainActivity::class.java))
                }
            } else {
                openProfileRegisterInformation()
            }
        })
    }

    fun sendSmsCodeValidation() {
        if (smsCode.equals(activityVerificationCodeEdit.textValue(), true)) {
            viewModel.sendSmsCodeValidation(
                activityVerificationCodeEdit.textValue(),
                getUserPhoneNumber()
            )
        } else Toast.makeText(this, "Invalid sms code", Toast.LENGTH_SHORT).show()
    }

    fun openProfileRegisterInformation() {

        val accountType =
            if (UserCreationProperties.isDeliverer())
                RegisterProfileDeliverInformationActivity::class.java
            else
                RegisterProfileSenderInformationActivity::class.java

        startActivity(
            Intent(
                getViewContext(),
                accountType
            )
        )
    }

    fun setEditTextListner() {
        activityVerificationCodeEdit.addTextChangedListener(
            object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                    if (s.toString().length == 4) {
                        sendSmsCodeValidation()
                    }
                }

                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {

                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                }

            }
        )

    }

}
