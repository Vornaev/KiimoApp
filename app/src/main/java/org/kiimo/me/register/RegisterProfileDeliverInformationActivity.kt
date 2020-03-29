package org.kiimo.me.register

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import kotlinx.android.synthetic.main.activity_register_profile_information.*
import kotlinx.android.synthetic.main.layout_edit_field_with_validation.view.*
import kotlinx.android.synthetic.main.layout_register_deliver_account.*
import kotlinx.android.synthetic.main.layout_register_personal_id.view.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.kiimo.me.R
import org.kiimo.me.main.MainActivity
import org.kiimo.me.register.model.UserAddressDataRequest
import org.kiimo.me.register.model.UserProfileInformationRequest
import org.kiimo.me.register.model.UserRegisterDataRequest
import org.kiimo.me.util.MediaManager
import org.kiimo.me.util.PreferenceUtils
import java.io.ByteArrayOutputStream

class RegisterProfileDeliverInformationActivity : RegisterProfileSenderInformationActivity() {

    private val MY_PERMISSION_CAMERA = 200
    private var personalIDPhotoURL = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        registerDeliverLayout.visibility = View.VISIBLE

        setListenerCamera()
    }


    override fun registerViewModelObservers() {
        viewModel.userProfileData.observe(
            this,
            Observer {
                activateUser()
                navigateToMain()
            }
        )
    }

    private fun activateUser() {
        viewModel.ativateUser()
    }

    fun navigateToMain() {
        startActivity(Intent(this, MainActivity::class.java))
    }

    override fun setFieldsHints() {
        super.setFieldsHints()

        layoutRegisterUserHouseNumber.EditTextFieldValidation.hint =
            getString(R.string.houne_number_hint)

        layoutRegisterProfileZipCode.EditTextFieldValidation.hint =
            getString(R.string.zip_cide_hint)
        layoutRegisterProfilePlace.EditTextFieldValidation.hint =
            getString(R.string.register_place_hint)
        layoutRegisterProfileCountryCode.EditTextFieldValidation.hint =
            getString(R.string.county_code_hint)

        layoutRegisterUserStreet.EditTextFieldValidation.hint =
            getString(R.string.street_hint)
    }


    override fun attachListenerValidators() {
        super.attachListenerValidators()
        atachListenerToField(
            layoutRegisterUserStreet.EditTextFieldValidation,
            ::validateStreet
        )
        atachListenerToField(
            layoutRegisterUserHouseNumber.EditTextFieldValidation,
            ::validateHouseNumber
        )
        atachListenerToField(
            layoutRegisterProfileZipCode.EditTextFieldValidation,
            ::validateZipCode
        )
        atachListenerToField(
            layoutRegisterProfilePlace.EditTextFieldValidation,
            ::validatePlace
        )

        atachListenerToField(
            layoutRegisterProfileCountryCode.EditTextFieldValidation,
            ::validateCountryCode
        )

    }

    override fun setButtonAction() {

        activityRegisterProfileNextButton.setOnClickListener {
            if (registerInformationValidation()) {
                viewModel.saveUserRegisterInformation(
                    userProfileInformationRequest = UserProfileInformationRequest(
                        address = UserAddressDataRequest(
                            countryCode = layoutRegisterProfileCountryCode.EditTextFieldValidation.textValue(),
                            houseNumber = layoutRegisterUserHouseNumber.EditTextFieldValidation.textValue(),
                            street = layoutRegisterProfilePlace.EditTextFieldValidation.textValue(),
                            zip = layoutRegisterProfileZipCode.EditTextFieldValidation.textValue()
                        ),
                        email = layoutRegisterUserEmail.EditTextFieldValidation.textValue(),
                        personalID = personalIDPhotoURL,
                        firstName = layoutRegisterUserFirstName.EditTextFieldValidation.textValue(),
                        lastName = layoutRegisterUserLastName.EditTextFieldValidation.textValue(),
                        userID = PreferenceUtils.getUserToken(this),
                        user = UserRegisterDataRequest()
                    )
                )
            }
        }

    }


    override fun registerInformationValidation(): Boolean {

        val validHouseNumber = validateHouseNumber()
        val validZipCode = validateZipCode()
        val validPlace = validatePlace()
        val validCountryCode = validateCountryCode()
        val validUser = super.registerInformationValidation()
        val validStreet = validateStreet()

        val validPhoto = validateImageField()


        val valid =
            validZipCode && validPlace && validCountryCode && validHouseNumber && validUser && validStreet && validPhoto
        return valid
    }


    private fun setListenerCamera() {

        viewModel.uploadPersonalID.observe(this, Observer {
            personalIDPhotoURL = it.imageUrl
            validateImageField()
        })

        activityRegisterProfileVerificationField.TextFieldPersonalID.setOnClickListener {
            MediaManager.showMediaOptionsDialog(this)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {

            val width = 480
            val height = 640

            if (requestCode == MediaManager.REQUEST_IMAGE_CAPTURE) {
                MediaManager.getBitmap(
                    width.toFloat(), height.toFloat()
                )?.apply {
                    onSuccessGetImage(this)
                }
            } else if (requestCode == MediaManager.REQUEST_IMAGE_PICK) {
                val uri = data?.data

                MediaManager.getBitmapGallery(
                    MediaStore.Images.Media.getBitmap(this.contentResolver, uri),
                    width, height
                )?.apply {
                    onSuccessGetImage(this)
                }
            }
        }
    }



    private fun onSuccessGetImage(bitmap: Bitmap) {
        activityRegisterProfileVerificationField.registerValidationImageVerificationPreview.visibility =
            View.VISIBLE
        activityRegisterProfileVerificationField.registerValidationImageVerificationPreview.setImageBitmap(
            bitmap
        )
        displayValidationStatus(
            activityRegisterProfileVerificationField,
            true
        )
        uploadBitmap(bitmap)
    }

    fun uploadBitmap(bitmap: Bitmap) {

        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
        var byteArray = stream.toByteArray()

        val fileRequest = RequestBody.create(
            MediaType.parse("image/*"),
            byteArray
        )

        val body = MultipartBody.Part.createFormData(
            "media",
            "${System.currentTimeMillis()}.jpg",
            fileRequest
        );

        viewModel.uploadPhotoPersonalID(body)

        stream.flush()
        stream.close()
    }

    private fun validateHouseNumber(): Boolean {
        val valid = layoutRegisterUserHouseNumber.EditTextFieldValidation.textValue().isNotBlank()
        displayValidationStatus(layoutRegisterUserHouseNumber, valid)

        return valid
    }

    private fun validateZipCode(): Boolean {
        val valid = layoutRegisterProfileZipCode.EditTextFieldValidation.textValue().isNotBlank()
        displayValidationStatus(layoutRegisterProfileZipCode, valid)

        return valid
    }

    private fun validateStreet(): Boolean {
        val valid = layoutRegisterUserStreet.EditTextFieldValidation.textValue().isNotBlank()
        displayValidationStatus(layoutRegisterUserStreet, valid)

        return valid
    }

    private fun validatePlace(): Boolean {
        val valid = layoutRegisterProfilePlace.EditTextFieldValidation.textValue().isNotBlank()
        displayValidationStatus(layoutRegisterProfilePlace, valid)

        return valid
    }


    private fun validateCountryCode(): Boolean {
        val valid =
            layoutRegisterProfileCountryCode.EditTextFieldValidation.textValue().isNotBlank()
        displayValidationStatus(layoutRegisterProfileCountryCode, valid)

        return valid
    }

    private fun validateImageField(): Boolean {

        val valid = personalIDPhotoURL.isNotBlank()

        displayValidationStatus(
            activityRegisterProfileVerificationField,
            personalIDPhotoURL.isNotBlank()
        )
        return valid

    }


}