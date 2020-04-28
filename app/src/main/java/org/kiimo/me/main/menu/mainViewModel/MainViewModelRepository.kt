package org.kiimo.me.main.menu.mainViewModel

import android.app.Activity
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.kiimo.me.BuildConfig
import org.kiimo.me.app.IBaseViewFeatures
import org.kiimo.me.main.fragments.model.deliveries.DeliveryCarrierItem
import org.kiimo.me.main.fragments.model.sender.SenderOrderListResponse
import org.kiimo.me.main.menu.model.CreditCardSaveRequest
import org.kiimo.me.main.menu.model.GetUserRequestModel
import org.kiimo.me.main.menu.model.UserProfileInformationResponse
import org.kiimo.me.main.sender.model.request.CreateDeliveryRequest
import org.kiimo.me.main.sender.model.request.PayRequest
import org.kiimo.me.main.sender.model.request.pay.PayResponse
import org.kiimo.me.main.sender.model.request.rate.RateDeliveryRequest
import org.kiimo.me.main.sender.model.response.SenderCreateDeliveryResponse
import org.kiimo.me.models.*
import org.kiimo.me.models.delivery.*
import org.kiimo.me.models.payment.PreferredPayResponse
import org.kiimo.me.models.payment.PreferredPaymentUser
import org.kiimo.me.register.model.*
import org.kiimo.me.service.network.client.KiimoAppClient
import org.kiimo.me.service.network.client.KiimoDeliverHttpClient
import org.kiimo.me.util.AppConstants
import org.kiimo.me.util.DialogUtils
import retrofit2.http.Multipart
import timber.log.Timber
import java.lang.Exception
import java.util.*

class MainViewModelRepository(
        private val userClient: KiimoAppClient,
        private val deliveryClient: KiimoDeliverHttpClient,
        private val viewFeatures: IBaseViewFeatures
) {

    private val disposableContainer: CompositeDisposable = CompositeDisposable()

    fun getUser(userProfileLiveData: MutableLiveData<UserProfileInformationResponse>) {
        disposableContainer.add(userClient.getUserByID(
            data = GetUserRequestModel(
                UserRegisterDataRequest(), viewFeatures.getUserToken()
            )
        )
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io()).subscribe(
                {
                    userProfileLiveData.postValue(it)
                }, {
                    viewFeatures.handleApiError(it)
                }
            ))
    }

    fun calculateDelivery(
            deliveryCalculateRequest: CalculateDeliveryRequest,
            calculateDeliveryData: MutableLiveData<CalculateDeliveryResponse>
    ) {
        disposableContainer.add(
            deliveryClient.deliveryCalculate(
                token = viewFeatures.getUserToken(),
                delivryCalculateRequest = deliveryCalculateRequest
            )
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io()).subscribe(
                    {
                        calculateDeliveryData.postValue(it)
                    }, {
                        viewFeatures.handleApiError(it)
                    }
                ))
    }
//
//
//    fun acceptDelivery(deliveryID: String, acceptLiveData: MutableLiveData<AcceptDeliveryResponse>) {
//        disposableContainer.add(
//            deliveryClient.acceptDelivery(
//                token = viewFeatures.getUserToken(),
//                deliveryID = deliveryID
//            )
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribeOn(Schedulers.io()).subscribe(
//                    {
//                        acceptLiveData.postValue(it)
//                    }, {
//                        viewFeatures.handleApiError(it)
//                    }
//                ))
//    }
//
//
//    fun pickUpDelivery(pickUpLiveData: MutableLiveData<PickUpDeliveryResponse>) {
//
//        disposableContainer.add(
//            deliveryClient.pickUpDelivery(
//                token = viewFeatures.getUserToken()
//            )
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribeOn(Schedulers.io()).subscribe(
//                    {
//                        pickUpLiveData.postValue(it)
//                    }, {
//                        viewFeatures.handleApiError(it)
//                    }
//                ))
//    }
//
//    fun dropOffDelivery(dropOffLiveData: MutableLiveData<DropOffDeliveryResponse>) {
//        disposableContainer.add(
//            deliveryClient.dropOffDelivery(
//                token = viewFeatures.getUserToken()
//            )
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribeOn(Schedulers.io()).subscribe(
//                    {
//                        dropOffLiveData.postValue(it)
//                    }, {
//                        viewFeatures.handleApiError(it)
//                    }
//                ))
//    }

    fun isValidDeliverer(liveData: MutableLiveData<IsValidDelivererResponse>) {

        disposableContainer.add(
            userClient.isValidDeliverer(
                activateUserRequest = ActivateUserRequest(
                    user = UserRegisterDataRequest(),
                    userId = viewFeatures.getUserToken()
                )
            )
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    {
                        liveData.postValue(it)
                    },
                    {
                        viewFeatures.handleApiError(it)
                    })
        )
    }


    fun <D, F> reactiveSubsribe(
            serviceMethod: (token: String, body: F) -> Observable<D>,
            data: MutableLiveData<D>,
            token: String,
            body: F
    ) {

        disposableContainer.add(
            serviceMethod.invoke(token, body)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io()).subscribe(
                    {
                        data.postValue(it)
                    },
                    {
                        viewFeatures.handleApiError(it)
                    })
        )
    }


    fun saveUserDeviceToken() {
        disposableContainer.add(
            deliveryClient.putDeviceToken(
                token = viewFeatures.getUserToken(),
                deviceToken = viewFeatures.getFcmToken()
            )
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io()).subscribe(
                    {
                        Timber.log(1, "success")
                    },
                    {
                        viewFeatures.handleApiError(it)
                    })
        )
    }

    fun putDeliveryType(deliveryType: DeliveryType) {
        disposableContainer.add(
            deliveryClient.putDeliveryType(
                token = viewFeatures.getUserToken(),
                deliveryType = deliveryType
            )
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io()).subscribe(
                    {

                        Timber.log(1, "success")
                    },
                    {
                        viewFeatures.handleApiError(it)
                    })
        )
    }

    fun getDirectionRoute(url: String, destData: MutableLiveData<DestinationData>) {
        disposableContainer.add(
            deliveryClient.getDestinationData(url)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { destData.postValue(it) },
                    { viewFeatures.handleApiError(it) })
        )
    }

    fun createPackageForDelivery(
            request: CreateDeliveryRequest,
            data: MutableLiveData<SenderCreateDeliveryResponse>
    ) {
        disposableContainer.add(
            deliveryClient.createPackageForDelivery(
                token = viewFeatures.getUserToken(),
                request = request
            )
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(
                    {
                        data.postValue(it)
                    },
                    {
                        viewFeatures.handleApiError(it)
                    }
                ))
    }

    fun onClear() {
        disposableContainer.clear()
    }

    fun saveUserData(
            userProfileInformationRequest: UserProfileInformationRequest,
            updateUserProfileLiveData: MutableLiveData<UserRegisterResponse>
    ) {
        disposableContainer.add(
            userClient.updateUserInformation(
                userProfileInformationRequest
            )
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(
                    {
                        updateUserProfileLiveData.postValue(it)
                    },
                    {
                        viewFeatures.handleApiError(it)
                    }
                ))
    }

    fun putStatus(status: Status, data: MutableLiveData<StatusResponse>) {
        disposableContainer.add(
            deliveryClient.putStatus("application/json", viewFeatures.getUserToken(), status)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    {
                        data.postValue(it)
                    },
                    {
                        viewFeatures.handleApiError(it)
                    })
        )
    }

    fun payForPackage(payRequest: PayRequest, data: MutableLiveData<PayResponse>) {
        disposableContainer.add(
            deliveryClient.payForPackage(
                token = viewFeatures.getUserToken(),
                payRequest = payRequest
            )
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    {
                        data.postValue(it)
                    },
                    {
                        viewFeatures.handleApiError(it)
                    })
        )
    }


    fun putLocation(locationModel: LocationModel) {
        disposableContainer.add(
            deliveryClient.putLocation(
                token = viewFeatures.getUserToken(),
                locationRequest = LocationRequest(locationModel)
            )
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    {
                        if (BuildConfig.DEBUG) {
                            viewFeatures.trackRequestSuccess("success PutLocation")
                        }
                    },
                    {
                        viewFeatures.handleApiError(it)
                    })
        )
    }


    fun savePreferredPaymentUser(
            preferredPaymentUser: PreferredPaymentUser,
            prefferedLiveData: MutableLiveData<PreferredPayResponse>
    ) {
        disposableContainer.add(
            deliveryClient.savePreferredPayment(
                token = viewFeatures.getUserToken(),
                preferedPay = preferredPaymentUser
            )
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    {
                        prefferedLiveData.postValue(it)
                        if (BuildConfig.DEBUG) {
                            viewFeatures.trackRequestSuccess("Preferred Payment Success")
                        }
                    },
                    {
                        viewFeatures.handleApiError(it)
                    })
        )
    }


    fun uploadMultiFormDataImage(
            type: String,
            data: MultipartBody.Part,
            photoLiveData: MutableLiveData<UploadImageResponse>
    ) {

        val type = RequestBody.create(
            okhttp3.MultipartBody.FORM, type
        )
        disposableContainer.add(
            deliveryClient.uploadMultipardData(data, type)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    photoLiveData.postValue(it)
                    if (BuildConfig.DEBUG) {
                        viewFeatures.trackRequestSuccess("Photo uploaded")
                    }
                },
                    {
                        viewFeatures.handleApiError(it)
                    })
        )
    }

    fun getDeliveryList(deliveryLiveData: MutableLiveData<MutableList<SenderOrderListResponse>>) {
        disposableContainer.add(
            deliveryClient.getDeliveriesList(
                token = viewFeatures.getUserToken()
            )
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    {
                        deliveryLiveData.postValue(it)
                    },
                    {
                        viewFeatures.handleApiError(it)
                    })
        )

    }

    fun getOrdersList(ordersLiveDta: MutableLiveData<MutableList<SenderOrderListResponse>>) {
        disposableContainer.add(
            deliveryClient.getOrdersList(
                token = viewFeatures.getUserToken()
            )
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    {
                        ordersLiveDta.postValue(it)
                        viewFeatures.trackRequestSuccess("orders list success")
                    },
                    {
                        viewFeatures.handleApiError(it)
                    })
        )
    }

    fun updateUserPhoto(photoUrl: String, updateData: MutableLiveData<UserRegisterResponse>) {
        disposableContainer.add(
            userClient.updateUserInformation(
                UserProfileUpdatePhotoRequest(
                    photoUrl,
                    viewFeatures.getUserToken(),
                    UserRegisterDataRequest()
                )
            )
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    {
                        //updateData.postValue(it)
                    }, {
                        viewFeatures.handleApiError(it)
                    }
                )
        )
    }

    fun updateUserFragmentFields(
            updateRequest: UserProfileFragmentUpdateRequest,
            updateData: MutableLiveData<UserRegisterResponse>) {
        disposableContainer.add(
            userClient.updateUserInformation(
                updateRequest
            )
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    {
                        updateData.postValue(it)
                        viewFeatures.trackRequestSuccess("photo saved")
                    }, {
                        viewFeatures.handleApiError(it)
                    }
                )
        )
    }

    fun rateUserForDelivery(rateDeliveryRequest: RateDeliveryRequest) {
        disposableContainer.add(
            deliveryClient.rateUser(
                token = viewFeatures.getUserToken(),
                rateDeliveryRequest = rateDeliveryRequest
            )
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    {
                        viewFeatures.trackRequestSuccess("photo saved")
                    }, {
                        viewFeatures.handleApiError(it)
                    }
                )
        )
    }

    fun saveCreditCard(
            creditCardSaveRequest: CreditCardSaveRequest,
            cardResponseData: MutableLiveData<BaseDeliveryResponse>,
            cardException: MutableLiveData<Throwable>) {
        disposableContainer.add(
            deliveryClient.saveCreditCard(
                token = viewFeatures.getUserToken(),
                cardSaveRequest = creditCardSaveRequest
            ).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    {
                        cardResponseData.postValue(BaseDeliveryResponse("", success = true))
                        viewFeatures.trackRequestSuccess("card saved")
                    }, {
                        viewFeatures.handleApiError(it)
                        cardException.postValue(it)
                    }
                )
        )
    }


    fun updateCreditCard(
            creditCardSaveRequest: CreditCardSaveRequest,
            cardResponseData: MutableLiveData<BaseDeliveryResponse>,
            cardException: MutableLiveData<Throwable>) {
        disposableContainer.add(
            deliveryClient.updateCreditCard(
                token = viewFeatures.getUserToken(),
                cardSaveRequest = creditCardSaveRequest
            ).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    {
                        cardResponseData.postValue(BaseDeliveryResponse("", true))
                        viewFeatures.trackRequestSuccess("card updated")
                    }, {
                        cardException.postValue(it)
                        viewFeatures.handleApiError(it)
                    }
                )
        )
    }

    fun saveCreditCardFieldsMultipart(
            creditCardSaveRequest: CreditCardSaveRequest,
            creditCardLiveData: MutableLiveData<BaseDeliveryResponse>,
            cardExceptionLiveData: MutableLiveData<Throwable>) {

    }

    fun saveCreditCardFields(
            creditCardSaveRequest: CreditCardSaveRequest,
            cardResponseData: MutableLiveData<BaseDeliveryResponse>,
            cardException: MutableLiveData<Throwable>) {
        disposableContainer.add(
            deliveryClient.saveCreditCardFields(
                token = viewFeatures.getUserToken(),
                cardNum = creditCardSaveRequest.cardNo,
                cardMonth = creditCardSaveRequest.expMonth.toInt(),
                cardYear = creditCardSaveRequest.expYear.toInt(),
                cv2 = creditCardSaveRequest.cv2.toInt()
            ).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    {
                        //cardResponseData.postValue(it)
                        viewFeatures.trackRequestSuccess("card saved")
                    }, {
                        viewFeatures.handleApiError(it)
                        cardException.postValue(it)
                    }
                )
        )
    }
}
