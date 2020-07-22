package org.kiimo.me.register

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.lifecycle.Observer
import kotlinx.android.synthetic.main.activity_register_sync_account.*
import kotlinx.android.synthetic.main.layout_phone_number_input.*
import org.kiimo.me.R
import org.kiimo.me.util.PreferenceUtils

class RegisterSyncAccountActivity : BaseRegistrationServicesActivity() {

    override fun getLayoutId(): Int {
        return R.layout.activity_register_sync_account
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        registerViewModelObservers()
        setPhoneListener()
        setButtonAction()
    }

    private fun setButtonAction() {

        activityRegisterSendCodeButton.setOnClickListener {
            val phoneNumber = activityRegisterCCpDialog.fullNumberWithPlus
            PreferenceUtils.saveUserPhoneNumber(this, phoneNumber)
            viewModel.sendPhoneNumber(phoneNumber)
        }
    }

    private fun registerViewModelObservers() {

        viewModel.tokenData.observe(this, Observer {

            val phoneNumber = activityRegisterCCpDialog.fullNumberWithPlus
            viewModel.sendSmsCodeToUser(phoneNumber)

            //send sms code api url
        })

        viewModel.smsCodeLiveData.observe(this, Observer {
            val intent = Intent(this, RegisterValidationCodeActivity::class.java)
            intent.putExtra(RegisterValidationCodeActivity.SMS_KEY_CODE, it.code)
            startActivity(intent)
        })
    }

    private fun setPhoneListener() {
        activityRegisterCCpDialog.registerCarrierNumberEditText(phoneNumberValueEditText)

        phoneNumberValueEditText.addTextChangedListener(
            object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                    if (s.toString().length >= 6) {
                        activityRegisterSendCodeButton.visibility = View.VISIBLE
                    } else {
                        activityRegisterSendCodeButton.visibility = View.GONE
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