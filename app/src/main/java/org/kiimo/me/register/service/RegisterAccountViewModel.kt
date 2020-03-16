package org.kiimo.me.register.service

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import org.kiimo.me.register.model.*

class RegisterAccountViewModel(private var repository: RegisterAccountRepository) : ViewModel() {

    var tokenData: MutableLiveData<String> = MutableLiveData()
    var userProfileData = MutableLiveData<UserRegisterResponse>()
    var smsCodeLiveData = MutableLiveData<UserRegisterResponse>()
    var smsValidationCodeLiveData = MutableLiveData<SmsValidationResponse>()
    var userLoginLiveData = MutableLiveData<UserLoginResponse>()
    var activateUserLiveData = MutableLiveData<StatusMessageDataResponse>()


    init {

    }

    fun sendPhoneNumber(phoneNumber: String) {
        repository.sendPhoneNumber(
            UserRegisterPhoneRequest(
                user = UserRegisterDataRequest(),
                username = phoneNumber
            ), tokenData
        )
    }

    fun saveUserRegisterInformation(userProfileInformationRequest: UserProfileInformationRequest) {
        repository.saveUserProfileInformation(userProfileInformationRequest, userProfileData)
    }

    fun sendSmsCodeValidation(smsCode: String, phoneNumber: String) {
        repository.validateSmsCode(smsCode, phoneNumber, smsValidationCodeLiveData)
    }

    override fun onCleared() {
        repository.onClear()
        super.onCleared()
    }

    fun userLogin() {
        repository.userLogin(userLoginLiveData)
    }

    fun sendSmsCodeToUser(number: String) {
        repository.sendSMSCodeToUser(number, smsCodeLiveData)
    }

    fun ativateUser() {
        repository.activateUser(activateUserLiveData)
    }
}

class RegsiterViewModelFactory(var repository: RegisterAccountRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return RegisterAccountViewModel(repository) as T
    }
}
