package org.kiimo.me.main.account

import android.app.AlertDialog
import android.app.Dialog
import android.graphics.Bitmap
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import androidx.databinding.DataBindingUtil
import kotlinx.android.synthetic.main.layout_edit_field_with_validation.view.*
import kotlinx.android.synthetic.main.layout_edit_field_with_validation.view.imageViewValidation
import kotlinx.android.synthetic.main.layout_edit_field_with_validation.view.textViewFieldRequired
import kotlinx.android.synthetic.main.layout_register_deliver_account.view.*
import kotlinx.android.synthetic.main.layout_register_personal_id.view.*
import org.kiimo.me.R
import org.kiimo.me.databinding.DialogChangeAccountTypeUserBinding
import org.kiimo.me.dialogs.BaseKiimoDialog
import org.kiimo.me.main.menu.mainViewModel.MainMenuViewModel
import org.kiimo.me.register.model.UserAddressDataRequest
import org.kiimo.me.register.model.UserProfileInformationRequest
import org.kiimo.me.register.model.UserRegisterDataRequest
import org.kiimo.me.util.IMediaManagerImages
import org.kiimo.me.util.MediaManager

class ChangeAccountTypeDialog : BaseKiimoDialog() {


    lateinit var binding: DialogChangeAccountTypeUserBinding
    lateinit var viewModel: MainMenuViewModel
    var userRequest: UserProfileInformationRequest? = null
    var personalIDImageUrl = ""

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        binding = DataBindingUtil.inflate(
            LayoutInflater.from(context),
            R.layout.dialog_change_account_type_user,
            null,
            false
        )

        val dialog = AlertDialog.Builder(
            requireContext(),
            R.style.ThemeOverlay_MaterialComponents_Dialog_Alert
        )
        dialog.setView(binding.root)
        setHints()
        setListeners()


        binding.changeAccountTypeSaveButton.setOnClickListener {
            if (validations()) {
                if (userRequest != null) {
                    userRequest!!.personalID = personalIDImageUrl
                    userRequest!!.address = UserAddressDataRequest(
                        countryCode = binding.registerDeliverLayout.layoutRegisterProfileCountryCode.EditTextFieldValidation.text.toString(),
                        houseNumber = binding.registerDeliverLayout.layoutRegisterUserHouseNumber.EditTextFieldValidation.text.toString(),
                        place = binding.registerDeliverLayout.layoutRegisterProfilePlace.EditTextFieldValidation.text.toString(),
                        street = binding.registerDeliverLayout.layoutRegisterUserStreet.EditTextFieldValidation.text.toString(),
                        zip = binding.registerDeliverLayout.layoutRegisterProfileZipCode.EditTextFieldValidation.text.toString()
                    )
                    userRequest!!.user = UserRegisterDataRequest()

                    viewModel.saveUser(userRequest!!)
                }
            }
        }
//        (activity as KiimoMainNavigationActivity).viewModel.sav


        return dialog.create()
    }

    fun setUser(
            userProfileInformationRequest: UserProfileInformationRequest,
            viewModel: MainMenuViewModel) {

        this.userRequest = userProfileInformationRequest
        this.viewModel = viewModel
    }

    fun setHints() {
        binding.registerDeliverLayout.layoutRegisterUserHouseNumber.EditTextFieldValidation.hint =
            getString(R.string.houne_number_hint)


        binding.registerDeliverLayout.layoutRegisterProfileZipCode.EditTextFieldValidation.hint =
            getString(R.string.zip_cide_hint)
        binding.registerDeliverLayout.layoutRegisterProfilePlace.EditTextFieldValidation.hint =
            getString(R.string.register_place_hint)
        binding.registerDeliverLayout.layoutRegisterProfileCountryCode.EditTextFieldValidation.hint =
            getString(R.string.county_code_hint)

        binding.registerDeliverLayout.layoutRegisterUserStreet.EditTextFieldValidation.hint =
            getString(R.string.street_hint)
    }

    fun setListeners() {
        binding.registerDeliverLayout.activityRegisterProfileVerificationField.TextFieldPersonalID.setOnClickListener {
            MediaManager.showMediaOptionsDialog(requireActivity() as IMediaManagerImages)
        }

        atachListenerToField(
            binding.registerDeliverLayout.layoutRegisterUserStreet.EditTextFieldValidation,
            ::validateStreet
        )
        atachListenerToField(
            binding.registerDeliverLayout.layoutRegisterUserHouseNumber.EditTextFieldValidation,
            ::validateHouseNumber
        )
        atachListenerToField(
            binding.registerDeliverLayout.layoutRegisterProfileZipCode.EditTextFieldValidation,
            ::validateZipCode
        )
        atachListenerToField(
            binding.registerDeliverLayout.layoutRegisterProfilePlace.EditTextFieldValidation,
            ::validatePlace
        )

        atachListenerToField(
            binding.registerDeliverLayout.layoutRegisterProfileCountryCode.EditTextFieldValidation,
            ::validateCountryCode
        )

    }

    fun onNewBitmap(bitmap: Bitmap) {
        binding.registerDeliverLayout.activityRegisterProfileVerificationField.registerValidationImageVerificationPreview.setImageBitmap(
            bitmap
        )
        binding.registerDeliverLayout.activityRegisterProfileVerificationField.registerValidationImageVerificationPreview.visibility =
            View.VISIBLE
    }

    fun setServerImageUrl(imageUrl: String) {
        personalIDImageUrl = imageUrl
        validateImageField()
    }

    fun validations(): Boolean {

        val validHouseNumber = validateHouseNumber()
        val validZipCode = validateZipCode()
        val validPlace = validatePlace()
        val validCountryCode = validateCountryCode()
        val validStreet = validateStreet()
        val validPhoto = validateImageField()


        val valid =
            validZipCode && validPlace && validCountryCode && validHouseNumber && validStreet && validPhoto
        return valid

    }

    private fun validateHouseNumber(): Boolean {
        val valid =
            binding.registerDeliverLayout.layoutRegisterUserHouseNumber.EditTextFieldValidation.textValue()
                .isNotBlank()
        displayValidationStatus(binding.registerDeliverLayout.layoutRegisterUserHouseNumber, valid)

        return valid
    }

    private fun validateZipCode(): Boolean {
        val valid =
            binding.registerDeliverLayout.layoutRegisterProfileZipCode.EditTextFieldValidation.textValue()
                .isNotBlank()
        displayValidationStatus(binding.registerDeliverLayout.layoutRegisterProfileZipCode, valid)

        return valid
    }

    private fun validateStreet(): Boolean {
        val valid =
            binding.registerDeliverLayout.layoutRegisterUserStreet.EditTextFieldValidation.textValue()
                .isNotBlank()
        displayValidationStatus(binding.registerDeliverLayout.layoutRegisterUserStreet, valid)

        return valid
    }

    private fun validatePlace(): Boolean {
        val valid =
            binding.registerDeliverLayout.layoutRegisterProfilePlace.EditTextFieldValidation.textValue()
                .isNotBlank()
        displayValidationStatus(binding.registerDeliverLayout.layoutRegisterProfilePlace, valid)

        return valid
    }


    private fun validateCountryCode(): Boolean {
        val valid =
            binding.registerDeliverLayout.layoutRegisterProfileCountryCode.EditTextFieldValidation.textValue()
                .isNotBlank()
        displayValidationStatus(
            binding.registerDeliverLayout.layoutRegisterProfileCountryCode,
            valid
        )

        return valid
    }

    private fun validateImageField(): Boolean {

        val valid = personalIDImageUrl.isNotBlank()

        displayValidationStatus(
            binding.registerDeliverLayout.activityRegisterProfileVerificationField,
            valid
        )
        return valid

    }

    protected fun displayValidationStatus(viewValidator: View, stateValid: Boolean) {
        viewValidator.imageViewValidation.visibility =
            if (!stateValid) View.VISIBLE else View.INVISIBLE
        viewValidator.textViewFieldRequired.visibility =
            if (!stateValid) View.VISIBLE else View.INVISIBLE
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

private fun EditText.textValue(): String {
    return this.text.toString()
}
