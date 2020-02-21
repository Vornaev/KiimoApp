package org.kiimo.me.main.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import org.kiimo.me.main.fragments.MapFragment
import org.kiimo.me.main.repositories.MapRepository
import org.kiimo.me.models.*

/**
 * The ViewModel for [MapFragment].
 */
class MapViewModel internal constructor(
    private val mapRepository: MapRepository
) : ViewModel() {

    val destinationData: LiveData<DestinationData?> = mapRepository.destinationDataLiveData

    val exception: LiveData<Throwable?> = mapRepository.exceptionLiveData

    val selfLiveData: LiveData<Self?> = mapRepository.selfLiveData

    val statusResponseLiveData: LiveData<StatusResponse?> = mapRepository.statusResponseLiveData

    val pickUpLiveData: LiveData<Boolean?> = mapRepository.pickUpLiveData

    val validateCodeLiveData: LiveData<Boolean?> = mapRepository.validateCodeLiveData

    val dropOffLiveData: LiveData<DropOffResponse?> = mapRepository.dropOffLiveData

    fun getDestinationData(url: String) {
        mapRepository.getDestinationData(url)
    }

    fun getSelf(token: String) {
        mapRepository.getSelf(token)
    }

    fun putLocation(token: String, locationModel: LocationModel) {
        mapRepository.putLocation(token, locationModel)
    }

    fun putDeliveryType(token: String, deliveryType: DeliveryType) {
        mapRepository.putDeliveryType(token, deliveryType)
    }

    fun putStatus(token: String, status: Status) {
        mapRepository.putStatus(token, status)
    }

    fun acceptDelivery(token: String, id: String) {
        mapRepository.acceptDelivery(token, id)
    }

    fun pickUp(token: String) {
        mapRepository.pickUp(token)
    }

    fun validateCode(token: String, code: ValidateCodeRequest) {
        mapRepository.validateCode(token, code)
    }

    fun dropOff(token: String, dropOffRequest: DropOffRequest) {
        mapRepository.dropOff(token, dropOffRequest)
    }

    override fun onCleared() {
        super.onCleared()
        mapRepository.onCleared()
    }
}