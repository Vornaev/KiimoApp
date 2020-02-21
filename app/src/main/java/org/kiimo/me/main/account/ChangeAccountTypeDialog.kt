package org.kiimo.me.main.account

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import kotlinx.android.synthetic.main.dialog_digit_code.view.*
import kotlinx.android.synthetic.main.layout_edit_field_with_validation.view.*
import kotlinx.android.synthetic.main.layout_register_deliver_account.*
import kotlinx.android.synthetic.main.layout_register_deliver_account.view.*
import org.kiimo.me.R
import org.kiimo.me.databinding.DialogChangeAccountTypeUserBinding
import org.kiimo.me.dialogs.BaseKiimoDialog
import org.kiimo.me.main.MainActivity
import org.kiimo.me.main.menu.KiimoMainNavigationActivity
import org.kiimo.me.main.menu.mainViewModel.MainMenuViewModel
import org.kiimo.me.register.model.UserAddressDataRequest
import org.kiimo.me.register.model.UserProfileInformationRequest
import org.kiimo.me.register.model.UserRegisterDataRequest
import org.kiimo.me.util.PreferenceUtils

class ChangeAccountTypeDialog : BaseKiimoDialog() {


    lateinit var binding: DialogChangeAccountTypeUserBinding
    lateinit var viewModel: MainMenuViewModel
    var userRequest: UserProfileInformationRequest? = null

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



        binding.changeAccountTypeSaveButton.setOnClickListener {

            if (userRequest != null) {

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
//        (activity as KiimoMainNavigationActivity).viewModel.sav


        return dialog.create()
    }

    fun setUser(
        userProfileInformationRequest: UserProfileInformationRequest,viewModel: MainMenuViewModel ){

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

}