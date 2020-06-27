package org.kiimo.me.main.sender.fragment

import android.app.Activity
import android.content.Intent
import android.location.Location
import android.os.Bundle
import android.transition.TransitionManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.AutocompleteActivity
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import io.reactivex.android.schedulers.AndroidSchedulers
import org.kiimo.me.R
import org.kiimo.me.app.BaseMainFragment
import org.kiimo.me.databinding.FragmentSenderLayoutBinding
import org.kiimo.me.main.fragments.DeliveryMapFragment
import org.kiimo.me.main.menu.mainViewModel.MainMenuViewModel
import org.kiimo.me.main.sender.SenderKiimoActivity
import org.kiimo.me.main.sender.map.ISenderMapFeatures
import org.kiimo.me.main.sender.map.SenderMapFeatures
import org.kiimo.me.main.sender.model.notifications.ConfirmPickUpNotification.ConfirmPickUpFcmData
import org.kiimo.me.main.sender.model.request.pay.PayResponse
import org.kiimo.me.models.LocationModel
import org.kiimo.me.services.LocationServicesKiimo
import org.kiimo.me.util.PreferenceUtils
import org.kiimo.me.util.RxBus
import org.kiimo.me.util.StringUtils
import java.util.concurrent.TimeUnit
import kotlin.math.roundToInt

class SenderMapFragment : BaseMainFragment() {


    lateinit var binding: FragmentSenderLayoutBinding
    var senderMapFeatures: ISenderMapFeatures? = null
    val viewModel: MainMenuViewModel by lazy { getNavigationActivity().viewModel }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        binding = FragmentSenderLayoutBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loadMap()
        setNavigation()
        setListeners()
        binding.isPickUpIntearact = true

        viewModel.destinationLiveData.observe(viewLifecycleOwner, Observer {
            Log.i("route", "recived route")
            senderMapFeatures?.onRouteReady(it)
        })

        // loadImageFromPreference()

        compositeDisposable.add(
            RxBus.listen(LocationServicesKiimo.LocEvent::class.java)
                .delay(2000, TimeUnit.MILLISECONDS).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    onServiceLocationEnabled()
                })

        viewModel.userProfileLiveData.observe(viewLifecycleOwner, Observer {
            loadImageProfile(it.photo)
        })


        viewModel.photoProfileLiveData.observe(requireActivity(), Observer {
            loadImageProfile(it.imageUrl)
        })
    }


    private fun loadImageFromPreference() {
        val userProfile = PreferenceUtils.getUserParsed(requireContext())
        userProfile?.let {
            loadImageProfile(it.photo)
        }
    }

    private fun loadImageProfile(url: String) {
        if (url.isBlank()) return
        Glide.with(this).load(url).placeholder(R.drawable.ic_user_profile)
            .apply(RequestOptions.circleCropTransform().override(0, 350))
            .into(binding.imageViewProfileDrawer)
    }

    private fun setNavigation() {
        binding.imageViewProfileDrawer.setOnClickListener {
            getNavigationActivity().onOpenDrawerClicked()
        }
    }


    private fun setListeners() {

        binding.layoutPin.pinPickUpConstraintLayout.setOnClickListener {
            openLocationSearchRequest(viewModel.senderProperties.pickUpAddressPoint.address)
            isPickUpClicked = true
        }

        binding.initialSenderLayout.senderPickUpButton.setOnClickListener {
            binding.isPickUpIntearact = false
            isPickUpClicked = false
            //binding.initialSenderLayout
        }

        binding.layoutPin.pinDropOffConstraintLayout.setOnClickListener {
            openLocationSearchRequest(viewModel.senderProperties.destinationAddressPoint.address)
            isPickUpClicked = false
        }

        binding.confirmButton.setOnClickListener {
            if (viewModel.senderProperties.hasPickUpAddress() && viewModel.senderProperties.hasDestination()) {
                (activity as SenderKiimoActivity).openCreateDeliveryItemDialog()
            }
        }

        binding.myLocationImageView.setOnClickListener {
            viewModel.isLocationSenderFromButton = true

            if (hasLocationPermission()) {
                LocationServicesKiimo.getUserDeviceLocation(
                    requireContext(),
                    ::onLocationUserFocused
                )
            } else {
                requestAccessFineLocationPermission()
            }
        }

    }

    private fun loadMap() {
        val mapFragment =
            childFragmentManager.findFragmentById(R.id.senderMap) as SupportMapFragment?
        mapFragment?.getMapAsync(mapReadyCallback)

    }

    private val mapReadyCallback = OnMapReadyCallback {
        requestAccessFineLocationPermission()
        senderMapFeatures = SenderMapFeatures(it, this)
        senderMapFeatures?.onMapReady()
    }

    override fun onPermissionLocationEnabled() {

        senderMapFeatures?.onPermissionGranted()

        LocationServicesKiimo.getUserDeviceLocation(
            requireContext(),
            if (!viewModel.isLocationSenderFromButton) ::onLocationReceived else ::onLocationUserFocused
        )
        viewModel.isLocationSenderFromButton = false
    }

    private fun onServiceLocationEnabled() {
        onPermissionLocationEnabled()
    }

    private fun onLocationUserFocused(deviceLoc: Location) {
        senderMapFeatures?.moveCameraToUserPos(deviceLoc)
    }


    private fun onLocationReceived(deviceLoc: Location) {

        viewModel.senderProperties.userLocation =
            LocationModel(deviceLoc.latitude, deviceLoc.longitude)

        viewModel.putLocation(LocationModel(deviceLoc.latitude, deviceLoc.longitude))

        viewModel.senderProperties.pickUpAddressPoint = MainMenuViewModel.AddressPoint(
            locationModel = LocationModel(
                deviceLoc.latitude,
                deviceLoc.longitude
            )
        )

        senderMapFeatures?.animateUserLocation(deviceLoc)
    }

    val myCountrryCode = {
        LocationServicesKiimo.getAddressForLocation(
            viewModel.senderProperties.userLocation?.toLatLng(),
            requireContext()
        )?.countryCode
    }

    private fun openLocationSearchRequest(query: String = "") {
        // Start the autocomplete intent.
        val intent = Autocomplete.IntentBuilder(
            AutocompleteActivityMode.FULLSCREEN, listOf(
                Place.Field.ID, Place.Field.NAME, Place.Field.ADDRESS,
                Place.Field.LAT_LNG
            )
        ).setInitialQuery(query).setCountry(myCountrryCode()).build(activity!!)

        startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE)
    }


    fun onSenderAddressReceived(addres: String) {
        binding.layoutPin.pickUpText = addres
        viewModel.senderProperties.pickUpAddressPoint.address = addres
        binding.initialSenderLayout.senderPickUpAddress = addres
    }

    var isPickUpClicked = true

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == DeliveryMapFragment.AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                data?.let {
                    val place = Autocomplete.getPlaceFromIntent(it)

                    if (isPickUpClicked) {
                        onPickUpLocationReceived(place)
                    } else {
                        onDestinationAddressRecived(place)
                    }
                }
            } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
                data?.let {
                    val status = Autocomplete.getStatusFromIntent(it)
                    Toast.makeText(activity, status.statusMessage, Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    fun onPickUpLocationReceived(place: Place) {

        place.latLng?.apply {

            val addresFormated = LocationServicesKiimo.getAddressForLocation(
                this,
                requireContext()
            )?.getAddressLine(0) ?: place.address ?: ""

            binding.layoutPin.pickUpText = addresFormated


            senderMapFeatures?.addPickUpMarker(this)
            viewModel.senderProperties.pickUpAddressPoint =
                MainMenuViewModel.AddressPoint(
                    address = addresFormated,
                    locationModel = LocationModel(
                        this.latitude,
                        this.longitude
                    )
                )
        }

        calculateRoute()

    }

    fun onDestinationAddressRecived(place: Place?) {


        binding.isButtonEnabled = true


        place?.latLng?.apply {
            //senderMapFeatures?.addPickUpMarker(this)
            val addressForrmatted: String? =
                LocationServicesKiimo.getAddressForLocation(this, requireContext())
                    ?.getAddressLine(0)

            binding.layoutPin.dropOffText = addressForrmatted ?: place.address ?: ""

            senderMapFeatures?.addDestinationMarker(this)
            viewModel.senderProperties.destinationAddressPoint =
                MainMenuViewModel.AddressPoint(
                    address = addressForrmatted!!,
                    locationModel = LocationModel(
                        this.latitude,
                        this.longitude
                    )
                )
        }

        calculateRoute()
    }

    fun calculateRoute() {

        if (!viewModel.senderProperties.hasDestination() && viewModel.senderProperties.hasPickUpAddress()) {
            return
        }

        viewModel.getDirectionsRoute(
            StringUtils.getDirectionsUrl(
                requireContext(),
                viewModel.senderProperties.pickUpAddressPoint.locationModel!!.toLatLng(),
                viewModel.senderProperties.destinationAddressPoint.locationModel!!.toLatLng(),
                "driving"
            )
        )
    }

    fun getSenderViewModel(): MainMenuViewModel {
        return viewModel
    }

    fun isDropOffActive(): Boolean {
        return !binding.isPickUpIntearact
    }

    fun receivedNotificationPickUp(deliveryPayedResponse: PayResponse) {
        binding.notificationPickUPReceived = true
        val carrierName =
            "${deliveryPayedResponse.carrier.firstName} ${deliveryPayedResponse.carrier.lastName}"

        if (!deliveryPayedResponse.carrier.photo.isNullOrBlank()) {
            Glide.with(this).load(deliveryPayedResponse.carrier.photo).override(0, 350).centerCrop()
                .placeholder(R.drawable.ic_user_profile)
                .into(binding.imageViewCarrier)
        }


        binding.carrierName.text = carrierName
        binding.pinLayoutDropOffPacakgeCarrier.pickUpText =
            deliveryPayedResponse.delivery.originAddress
        binding.pinLayoutDropOffPacakgeCarrier.dropOffText =
            deliveryPayedResponse.delivery.destinationAddress
        binding.carrierNameDescription.text = "Its on his way to the package"
    }

    fun receviedCodePickUP(deliveryAccepted: ConfirmPickUpFcmData) {

        if (!deliveryAccepted.carrier.photo.isBlank()) {
            Glide.with(this).load(deliveryAccepted.carrier.photo).override(0, 350).centerCrop()
                .placeholder(R.drawable.ic_user_profile)
                .into(binding.imageViewCarrier)
        }
        binding.notificationPickUPReceived = true
        binding.notificationCodeReceived = true


        binding.carrierPickUpCodeValue.text = deliveryAccepted.code

        val carrierName = "${deliveryAccepted.carrier.firstName} ${deliveryAccepted.carrier.lastName}"

        val timeText = "$carrierName will arrive in ${viewModel.senderProperties.deliveryPrice.minutes.roundToInt()} minutes"

        binding.carrierNameDescription.text = timeText

        binding.pinLayoutDropOffPacakgeCarrier.pickUpText = deliveryAccepted.delivery.originAddress

        binding.pinLayoutDropOffPacakgeCarrier.dropOffText = deliveryAccepted.delivery.destinationAddress

        binding.carrierName.text = carrierName
    }

    fun finishedDelivery() {
        binding.notificationCodeReceived = false
        binding.notificationPickUPReceived = false
    }

    fun shouldResetFragment() {
        binding.notificationCodeReceived = false
        binding.notificationPickUPReceived = false
        viewModel.senderProperties = MainMenuViewModel.SenderProperties()
    }

}
