package org.kiimo.me.main.menu.mainViewModel

import android.location.Location
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import okhttp3.MultipartBody
import org.kiimo.me.main.fragments.model.deliveries.DeliveryCarrierItem
import org.kiimo.me.main.fragments.model.sender.SenderOrderListResponse
import org.kiimo.me.main.menu.model.CreditCardModel
import org.kiimo.me.main.menu.model.CreditCardSaveRequest
import org.kiimo.me.main.menu.model.UserProfileInformationResponse
import org.kiimo.me.main.sender.model.request.CreateDeliveryRequest
import org.kiimo.me.main.sender.model.request.Packages
import org.kiimo.me.main.sender.model.request.PayRequest
import org.kiimo.me.main.sender.model.request.pay.PayResponse
import org.kiimo.me.main.sender.model.request.rate.RateDeliveryRequest
import org.kiimo.me.main.sender.model.response.SenderCreateDeliveryResponse
import org.kiimo.me.models.*
import org.kiimo.me.models.delivery.*
import org.kiimo.me.models.payment.PreferredPayResponse
import org.kiimo.me.models.payment.PreferredPaymentUser
import org.kiimo.me.register.model.IsValidDelivererResponse
import org.kiimo.me.register.model.UserProfileFragmentUpdateRequest
import org.kiimo.me.register.model.UserProfileInformationRequest
import org.kiimo.me.register.model.UserRegisterResponse
import kotlin.math.roundToInt


class MainMenuViewModel(private var repository: MainViewModelRepository) : ViewModel() {

    val userProfileLiveData = MutableLiveData<UserProfileInformationResponse>()
    val updateUserProfileLiveData = MutableLiveData<UserRegisterResponse>()
    val calculateDeliveryLiveData = MutableLiveData<CalculateDeliveryResponse>()
    val pickUpDeliveryLiveData = MutableLiveData<PickUpDeliveryResponse>()
    var dropOffDeliveryLiveData = MutableLiveData<DropOffDeliveryResponse>()
    var acceptDeliveryLiveData = MutableLiveData<AcceptDeliveryResponse>()
    var isValidDelivererLiveData = MutableLiveData<IsValidDelivererResponse>()
    var senderProperties = SenderProperties()
    val destinationLiveData = MutableLiveData<DestinationData>()
    val statusLiveData = MutableLiveData<StatusResponse>()
    val payPackageLiveData = MutableLiveData<PayResponse>()
    val photoPackageLiveData = MutableLiveData<UploadImageResponse>()
    val photoProfileLiveData = MutableLiveData<UploadImageResponse>()
    val signatureLiveData = MutableLiveData<UploadImageResponse>()
    val creditCardLiveData = MutableLiveData<BaseDeliveryResponse>()
    val cardExceptionLiveData = MutableLiveData<Throwable>()

    val userProfileFieldsUpdateLiveData = MutableLiveData<UserRegisterResponse>()

    val preferredPayLiveData = MutableLiveData<PreferredPayResponse>()


    var isLocationSenderFromButton = false

    fun setPackageSize(packageID: String) {
        senderProperties.packageDescritpion.packageSizeId = packageID
    }

    fun getUser() {
        repository.getUser(userProfileLiveData)
    }

    fun putDeviceToken() {
        repository.saveUserDeviceToken()
    }

    fun getDirectionsRoute(url: String) {
        repository.getDirectionRoute(url, destinationLiveData)
    }

    fun calculateDelivery(deliveryRequest: CalculateDeliveryRequest) {
        repository.calculateDelivery(deliveryRequest, calculateDeliveryLiveData)
    }

    fun putStatus(status: Status) {
        repository.putStatus(status, statusLiveData)
    }

    fun putLocation(locationModel: LocationModel) {
        repository.putLocation(locationModel)
    }

    fun rateUserForDelivery(rateDeliveryRequest: RateDeliveryRequest) {
        repository.rateUserForDelivery(rateDeliveryRequest)
    }


    val deliveryListLiveData = MutableLiveData<MutableList<SenderOrderListResponse>>()
    fun getDeliveryList() {
        repository.getDeliveryList(deliveryListLiveData)
    }

    val ordersListLiveData = MutableLiveData<MutableList<SenderOrderListResponse>>()
    fun getOrdersList() {
        repository.getOrdersList(ordersListLiveData)
    }

    fun saveCreditCard(creditCardModel: CreditCardModel) {

    }

    fun savePreferredPaymentType(preferredPaymentUser: PreferredPaymentUser) {
        repository.savePreferredPaymentUser(preferredPaymentUser, preferredPayLiveData)
    }

    fun acceptDelivery(deliveryID: String) {
        // repository.acceptDelivery(deliveryID, acceptDeliveryLiveData)
    }

    fun pickUpDelivery() {
        // repository.pickUpDelivery(pickUpDeliveryLiveData)
    }

    fun dropOfDelivery() {
        // repository.dropOffDelivery(dropOffDeliveryLiveData)
    }

    fun isValidDeliverer() {
        repository.isValidDeliverer(isValidDelivererLiveData)
    }

    fun saveUser(userProfileInformationRequest: UserProfileInformationRequest) {
        repository.saveUserData(userProfileInformationRequest, updateUserProfileLiveData)
    }

    fun payForFackage() {
        val payRequest = PayRequest()
        repository.payForPackage(payRequest, payPackageLiveData)
    }


    val createDelPackageCreateLData = MutableLiveData<SenderCreateDeliveryResponse>()
    fun createPackageForDelivery(request: CreateDeliveryRequest) {
        repository.createPackageForDelivery(request, createDelPackageCreateLData)
    }

    override fun onCleared() {
        repository.onClear()
        super.onCleared()
    }

    fun putDeliveryType(deliveryType: DeliveryType = DeliveryType("5c035544-347d-4a4c-9e13-09072360ad34")) {
        repository.putDeliveryType(deliveryType)
    }

    fun uploadPhotoForPackage(body: MultipartBody.Part) {
        repository.uploadMultiFormDataImage(Type.Packages, body, photoPackageLiveData)
    }

    fun uploadPhotoForUser(body: MultipartBody.Part) {
        repository.uploadMultiFormDataImage(Type.Users, body, photoProfileLiveData)
    }

    fun updateUserProfilePhoto(photoUrl: String) {
        repository.updateUserPhoto(photoUrl, userProfileFieldsUpdateLiveData)
    }

    fun updateUserNameProfile(updateRequest: UserProfileFragmentUpdateRequest) {
        repository.updateUserFragmentFields(updateRequest, userProfileFieldsUpdateLiveData)
    }

    fun saveCreditCard(creditCardSaveRequest: CreditCardSaveRequest) {
        repository.saveCreditCard(creditCardSaveRequest, creditCardLiveData, cardExceptionLiveData)
    }

    fun saveCreditCardFields(creditCardSaveRequest: CreditCardSaveRequest){
        repository.saveCreditCardFields(creditCardSaveRequest, creditCardLiveData, cardExceptionLiveData)
    }

    fun updateCreditCardData(creditCardSaveRequest: CreditCardSaveRequest) {
        repository.updateCreditCard(
            creditCardSaveRequest,
            creditCardLiveData,
            cardExceptionLiveData
        )
    }

    fun uploadSignaturePhoto(body: MultipartBody.Part) {
        repository.uploadMultiFormDataImage(Type.Signatures, body, signatureLiveData)
    }


    val personalIDLveData = MutableLiveData<UploadImageResponse>()
    fun uploadPersonalIDPhoto(body: MultipartBody.Part) {
        repository.uploadMultiFormDataImage(Type.PersionalID, body, personalIDLveData)
    }


    class SenderProperties {
        var isLocationFromButton = false
        var isPickUpInteraction = true
        var pickUpAddressPoint: AddressPoint = AddressPoint()
        var destinationAddressPoint: AddressPoint = AddressPoint()
        var userLocation: LocationModel? = null
        var packageDescritpion: Packages = Packages()
        var caluclatedPrice: String = "0"

        fun setPickUpPin(pinOptions: AddressPoint) {
            this.pickUpAddressPoint = pinOptions
        }

        fun setDestionationPin(pinOptions: AddressPoint) {
            this.destinationAddressPoint = pinOptions
        }

        fun hasDestination(): Boolean {
            return this.destinationAddressPoint.locationModel != null
        }

        fun hasPickUpAddress(): Boolean {
            return this.pickUpAddressPoint.locationModel != null
        }

        fun distanceToRoute(): Float {
            return distanceinKM(
                pickUpAddressPoint.locationModel!!.toGoogleLoc("pickUP"),
                destinationAddressPoint.locationModel!!.toGoogleLoc("dropOff")
            ).toFloat()
        }

        fun distanceinKM(source: Location, dest: Location): Double {
            return (source.distanceTo(dest).toDouble())
        }

        fun Float.round(decimals: Int): Double {
            var multiplier = 1.0
            repeat(decimals) { multiplier *= 10 }
            return kotlin.math.round(this * multiplier) / multiplier
        }

        fun getCreatePackageRequest(): CreateDeliveryRequest {
            val req = CreateDeliveryRequest(
                destination = destinationAddressPoint.locationModel,
                destinationAddress = destinationAddressPoint.address,
                distanceByFoot = this.distanceToRoute().roundToInt(),
                distanceByCar = this.distanceToRoute().roundToInt(),
                distanceByKickScooter = distanceToRoute().roundToInt(),
                distanceByBike = distanceToRoute().roundToInt(),
                originAddress = this.pickUpAddressPoint.address,
                origin = this.pickUpAddressPoint.locationModel,
                packages = packageDescritpion
            )
            return req
        }


        fun getCalculateRequest(): CalculateDeliveryRequest {
            return CalculateDeliveryRequest(
                origin = pickUpAddressPoint.locationModel,
                destination = destinationAddressPoint.locationModel
            )
        }
    }

    data class AddressPoint(
            var address: String = "",
            var locationModel: LocationModel? = null
    )
}


