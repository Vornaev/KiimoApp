package org.kiimo.me.register

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import androidx.lifecycle.Observer
import kotlinx.android.synthetic.main.activity_register_profile_information.*
import kotlinx.android.synthetic.main.layout_edit_field_with_validation.view.*
import org.kiimo.me.R
import org.kiimo.me.main.menu.KiimoMainNavigationActivity
import org.kiimo.me.main.sender.SenderKiimoActivity
import org.kiimo.me.register.model.UserAddressDataRequest
import org.kiimo.me.register.model.UserProfileInformationRequest
import org.kiimo.me.register.model.UserRegisterDataRequest
import org.kiimo.me.service.network.client.RetrofitConsts
import org.kiimo.me.util.PreferenceUtils

open class RegisterProfileSenderInformationActivity : BaseRegistrationServicesActivity() {


    override fun getLayoutId(): Int {
        return R.layout.activity_register_profile_information
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setFieldsHints()
        loadPhoneNumber()
        registerViewModelObservers()
        setButtonAction()
        attachListenerValidators()

    }

    protected open fun attachListenerValidators() {
        atachListenerToField(
            layoutRegisterUserFirstName.EditTextFieldValidation,
            ::validateFirstName
        )
        atachListenerToField(
            layoutRegisterUserLastName.EditTextFieldValidation,
            ::validateLastName
        )
        atachListenerToField(
            layoutRegisterUserEmail.EditTextFieldValidation,
            ::validateEmail
        )
    }

    protected open fun setFieldsHints() {

        layoutRegisterUserFirstName.EditTextFieldValidation.hint =
            getString(R.string.first_name_hint)
        layoutRegisterUserLastName.EditTextFieldValidation.hint = getString(R.string.last_name)
        layoutRegisterUserEmail.EditTextFieldValidation.hint = getString(R.string.email)

    }

    protected open fun setButtonAction() {

        activityRegisterProfileNextButton.setOnClickListener {
            if (registerInformationValidation()) {
                viewModel.saveUserRegisterInformation(
                    userProfileInformationRequest = UserProfileInformationRequest(
                        address = UserAddressDataRequest(),
                        email = layoutRegisterUserEmail.EditTextFieldValidation.textValue(),
                        firstName = layoutRegisterUserFirstName.EditTextFieldValidation.textValue(),
                        lastName = layoutRegisterUserLastName.EditTextFieldValidation.textValue(),
                        userID = PreferenceUtils.getUserToken(this),
                        user = UserRegisterDataRequest()
                    )
                )
            } else {
                startActivity(Intent(this, SenderKiimoActivity::class.java))
            }
        }
    }

    protected open fun registerInformationValidation(): Boolean {

        val validEmail = validateEmail()
        val validFirstName = validateFirstName()
        val validLastName = validateLastName()

        return validEmail && validFirstName && validLastName
    }

    fun validateEmail(): Boolean {
        val validEmail = layoutRegisterUserEmail.EditTextFieldValidation.textValue().isNotEmpty()
        displayValidationStatus(layoutRegisterUserEmail, validEmail)

        return validEmail
    }

    fun validateFirstName(): Boolean {
        val validFirstName =
            layoutRegisterUserFirstName.EditTextFieldValidation.textValue().isNotEmpty()
        displayValidationStatus(layoutRegisterUserFirstName, validFirstName)

        return validFirstName
    }

    fun validateLastName(): Boolean {
        val validLastName =
            layoutRegisterUserLastName.EditTextFieldValidation.textValue().isNotEmpty()
        displayValidationStatus(layoutRegisterUserLastName, validLastName)

        return validLastName
    }


    protected fun displayValidationStatus(viewValidator: View, stateValid: Boolean) {
        viewValidator.imageViewValidation.visibility =
            if (!stateValid) View.VISIBLE else View.INVISIBLE
        viewValidator.textViewFieldRequired.visibility =
            if (!stateValid) View.VISIBLE else View.INVISIBLE
    }

    protected open fun registerViewModelObservers() {
        viewModel.userProfileData.observe(
            this,
            Observer {
                if (it.status == RetrofitConsts.SUCCESS_CODE) {
                    viewModel.ativateUser()
                    startActivity(Intent(this, SenderKiimoActivity::class.java))
                }
            }
        )
    }

    private fun loadPhoneNumber() {
        val phoneNumber = PreferenceUtils.getUserPhoneNumber(this)
        activityRegisterProfileNumberEditText.setText(phoneNumber)
    }


    protected fun atachListenerToField(field: EditText, listener: () -> Boolean) {
        field.addTextChangedListener(
            object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                    listener()
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

}
