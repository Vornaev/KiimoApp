package org.kiimo.me.main.repositories

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import org.kiimo.me.BuildConfig
import org.kiimo.me.app.App
import org.kiimo.me.models.*
import org.kiimo.me.service.network.client.KiimoDeliverHttpClient
import javax.inject.Inject

class MapRepository {

    init {
        App.instance.getAppComponent().inject(this)
    }

    @Inject
    lateinit var kiimoDeliverHttpClient: KiimoDeliverHttpClient

    private val disposableContainer: CompositeDisposable = CompositeDisposable()

    private val destinationDataMutableLiveData: MutableLiveData<DestinationData?> =
        MutableLiveData()
    private val exceptionMutableLiveData: MutableLiveData<Throwable?> = MutableLiveData()
    private val selfMutableLiveData: MutableLiveData<Self?> = MutableLiveData()
    private val statusResponseMutableLiveData: MutableLiveData<StatusResponse?> = MutableLiveData()
    private val pickUpMutableLiveData: MutableLiveData<Boolean?> = MutableLiveData()
    private val validateCodeMutableLiveData: MutableLiveData<Boolean?> = MutableLiveData()
    private val dropOffMutableLiveData: MutableLiveData<DropOffResponse?> = MutableLiveData()

    val destinationDataLiveData: LiveData<DestinationData?> = destinationDataMutableLiveData
    val exceptionLiveData: LiveData<Throwable?> = exceptionMutableLiveData
    val selfLiveData: LiveData<Self?> = selfMutableLiveData
    val statusResponseLiveData: LiveData<StatusResponse?> = statusResponseMutableLiveData
    val pickUpLiveData: LiveData<Boolean?> = pickUpMutableLiveData
    val validateCodeLiveData: LiveData<Boolean?> = validateCodeMutableLiveData
    val dropOffLiveData: LiveData<DropOffResponse?> = dropOffMutableLiveData

    fun getDestinationData(url: String) {
        disposableContainer.add(
            kiimoDeliverHttpClient.getDestinationData(url)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { destinationDataMutableLiveData.postValue(it) },
                    { exceptionMutableLiveData.postValue(it) })
        )
    }

    fun getSelf(token: String) {
        disposableContainer.add(
            kiimoDeliverHttpClient.getSelf(token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    {
                        selfMutableLiveData.value = it
                    },
                    {
                        selfMutableLiveData.value = null
                        exceptionMutableLiveData.value = it
                    })
        )
    }

    fun putLocation(token: String, locationModel: LocationModel) {
        disposableContainer.add(
            kiimoDeliverHttpClient.putLocation(
                "application/json",
                token,
                LocationRequest(locationModel)
            )
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    {
                        println()
                    },
                    {
                        println()
                    })
        )
    }

    fun putDeliveryType(token: String, deliveryType: DeliveryType) {
        disposableContainer.add(
            kiimoDeliverHttpClient.putDeliveryType("application/json", token, deliveryType)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    {
                        println()
                    },
                    {
                        println()
                    })
        )
    }

    fun putStatus(token: String, status: Status) {
        disposableContainer.add(
            kiimoDeliverHttpClient.putStatus("application/json", token, status)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    {
                        statusResponseMutableLiveData.value = it
                    },
                    {
                        statusResponseMutableLiveData.value = null
                        exceptionMutableLiveData.value = it
                    })
        )
    }

    fun acceptDelivery(token: String, id: String) {
        disposableContainer.add(
            kiimoDeliverHttpClient.acceptDelivery(token, id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    {
                        if (BuildConfig.DEBUG) {
                            Toast.makeText(
                                App.instance.applicationContext,
                                "Success Accepet deliverry",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                        Log.i("success", "")
                    },
                    {
                        exceptionMutableLiveData.value = it
                    })
        )
    }

    fun pickUp(token: String) {
        disposableContainer.add(
            kiimoDeliverHttpClient.pickUp(token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { pickUpMutableLiveData.value = true },
                    {
                        pickUpMutableLiveData.value = false
                        exceptionMutableLiveData.value = it
                    })
        )
    }

    fun validateCode(token: String, code: ValidateCodeRequest) {
        disposableContainer.add(
            kiimoDeliverHttpClient.validateCode(token, code)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { validateCodeMutableLiveData.value = it?.success ?: false },
                    {
                        validateCodeMutableLiveData.value = false
                        exceptionMutableLiveData.value = it
                    })
        )
    }

    fun dropOff(token: String, dropOffRequest: DropOffRequest) {
        disposableContainer.add(
            kiimoDeliverHttpClient.dropOff(token, dropOffRequest)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { dropOffMutableLiveData.value = it },
                    {
                        dropOffMutableLiveData.value = null
                        exceptionMutableLiveData.value = it
                    })
        )
    }


    fun isValidDeliverer() {
//        kiimoDeliverHttpClient.isValidDeliverer()
//            .subscribeOn(Schedulers.io())
//            .observeOn(AndroidSchedulers.mainThread())
//            .subscribe(
//                {
//                    selfMutableLiveData.value = it },
//                {
//                    selfMutableLiveData.value = null
//                    exceptionMutableLiveData.value = it
//                })
    }


    fun onCleared() {
        disposableContainer.clear()
    }

    companion object {
        // For Singleton instantiation
        @Volatile
        private var instance: MapRepository? = null

        fun getInstance() =
            instance ?: synchronized(this) {
                instance ?: MapRepository().also { instance = it }
            }
    }
}