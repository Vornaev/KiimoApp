package org.kiimo.me.register.service

import android.util.Log
import androidx.lifecycle.MutableLiveData
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import org.kiimo.me.app.IBaseViewFeatures
import org.kiimo.me.register.model.*
import org.kiimo.me.service.network.client.KiimoAppClient
import org.kiimo.me.service.network.client.KiimoDeliverHttpClient
import org.kiimo.me.util.PreferenceUtils

class RegisterAccountRepository(
    private val client: KiimoAppClient,
    private val deliverHttpClient: KiimoDeliverHttpClient,
    private val viewFeatures: IBaseViewFeatures
) {

    private val disposableContainer: CompositeDisposable = CompositeDisposable()


    fun sendPhoneNumber(
        registerPhoneRequest: UserRegisterPhoneRequest,
        data: MutableLiveData<String>
    ) {
        disposableContainer.add(
            client.registerUserPhoneNumber(
                registerPhoneRequest
            )
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    {
                        PreferenceUtils.saveUserPassword(viewFeatures.getViewContext(), registerPhoneRequest.password)
                        PreferenceUtils.saveUseToken(viewFeatures.getViewContext(), it.userID ?: "")
                        UserCreationProperties.handleResponse(it)
                        data.postValue(it.userID)

                    }, {
                        viewFeatures.handleApiError(it)
                    }
                ))
    }

    fun saveUserProfileInformation(
        userProfileInformationRequest: UserProfileInformationRequest,
        userResponseData: MutableLiveData<UserRegisterResponse>
    ) {
        disposableContainer.add(
            client.updateUserInformation(
                userProfileInformationRequest
            )
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    {
                        userResponseData.postValue(it)

                    }, {
                        viewFeatures.handleApiError(it)
                    }
                ))
    }

    fun sendSMSCodeToUser(phone: String, smsLiveData: MutableLiveData<UserRegisterResponse>
    ) {
        disposableContainer.add(
            deliverHttpClient.sendCode(
              smsCodeRequest =  UserSmsCodeRequest(phone)
            ).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    {
                        smsLiveData.postValue(it)
                    },
                    {
                        viewFeatures.handleApiError(it)
                    }
                ))
    }


    fun validateSmsCode(
        smsCode: String,
        phone: String,
        smsLiveData: MutableLiveData<SmsValidationResponse>
    ) {
        disposableContainer.add(
            deliverHttpClient.validateSmsCode(
                userID = viewFeatures.getUserToken(),
                smsCodeRequest = SmsValidationRequest(smsCode = smsCode, username = phone)
            ).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    {
                        smsLiveData.postValue(it)
                    },
                    {
                        viewFeatures.handleApiError(it)
                    }
                ))
    }


    fun userLogin(userLoginLiveData: MutableLiveData<UserLoginResponse>) {
        disposableContainer.add(
            client.userLogin(
                UserLoginRequest(
                    UserRegisterDataRequest(),
                    viewFeatures.getUserPhoneNumber()
                )
            )
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    {
                        userLoginLiveData.postValue(it)
                        if (it.existUser && it.userID != null) {
                            if (it.userID.isNotEmpty())
                                PreferenceUtils.saveUseToken(
                                    viewFeatures.getViewContext(),
                                    it.userID
                                )
                        }
                    },
                    {
                        viewFeatures.handleApiError(it)
                    }
                ))
    }

    fun activateUser(activateUserLiveData: MutableLiveData<StatusMessageDataResponse>) {
        disposableContainer.add(
            client.activateUser(
                ActivateUserRequest(
                    user = UserRegisterDataRequest(),
                    userId = viewFeatures.getUserToken()
                )
            )
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    {
                        Log.i("activateUser", it.message)
                    },
                    {
                        viewFeatures.handleApiError(it)
                    }
                ))
    }


    fun onClear() {
        disposableContainer.dispose()
    }
}