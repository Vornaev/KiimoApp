package org.kiimo.me.main.sender.fragment

import android.app.Activity
import android.content.Intent
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.AutocompleteActivity
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import kotlinx.android.synthetic.main.fragment_sender_layout.*
import org.kiimo.me.R
import org.kiimo.me.app.BaseMainFragment
import org.kiimo.me.databinding.FragmentSenderLayoutBinding
import org.kiimo.me.main.fragments.MapFragment
import org.kiimo.me.main.menu.mainViewModel.MainMenuViewModel
import org.kiimo.me.main.sender.SenderKiimoActivity
import org.kiimo.me.main.sender.map.ISenderMapFeatures
import org.kiimo.me.main.sender.map.SenderMapFeatures
import org.kiimo.me.main.sender.model.notifications.ConfirmPickUpNotification.ConfirmPickUpFcmData
import org.kiimo.me.main.sender.model.request.pay.PayResponse
import org.kiimo.me.models.LocationModel
import org.kiimo.me.models.events.ProfilePhotoEvent
import org.kiimo.me.services.LocationServicesKiimo
import org.kiimo.me.util.PreferenceUtils
import org.kiimo.me.util.RxBus
import org.kiimo.me.util.StringUtils

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

        val userProf = PreferenceUtils.getUserParsed(requireContext())
        if (userProf.photo.isNotBlank()) {
            Glide.with(this).load(userProf.photo).override(350, 0).centerCrop()
                .into(binding.imageViewProfileDrawer)
        }

        viewModel.photoProfileLiveData.observe(requireActivity(), Observer {
            Glide.with(this).load(it.imageUrl).override(350, 0).centerCrop()
                .into(binding.imageViewProfileDrawer)
        })
    }

    private fun setNavigation() {
        binding.imageViewProfileDrawer.setOnClickListener {
            getNavigationActivity().onOpenDrawerClicked()
        }
    }


    private fun setListeners() {

        binding.layoutPin.pinPickUpConstraintLayout.setOnClickListener {
            openLocationSearchRequest()
            isPickUpClicked = true
        }

        binding.initialSenderLayout.senderPickUpButton.setOnClickListener {
            binding.isPickUpIntearact = false
            isPickUpClicked = false
            //binding.initialSenderLayout
        }

        binding.layoutPin.pinDropOffConstraintLayout.setOnClickListener {
            openLocationSearchRequest()
            isPickUpClicked = false
        }

        binding.confirmButton.setOnClickListener {

            (activity as SenderKiimoActivity).openCreateDeliveryItemDialog()
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
        LocationServicesKiimo.getUserDeviceLocation(requireContext(), ::onLocationReceived)
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

    private fun openLocationSearchRequest() {
        // Start the autocomplete intent.
        val intent = Autocomplete.IntentBuilder(
            AutocompleteActivityMode.FULLSCREEN, listOf(
                Place.Field.ID, Place.Field.NAME, Place.Field.ADDRESS,
                Place.Field.LAT_LNG
            )
        ).build(activity!!)

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
        if (requestCode == MapFragment.AUTOCOMPLETE_REQUEST_CODE) {
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
            Glide.with(this).load(deliveryPayedResponse.carrier.photo).override(350, 0).centerCrop()
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

        if (!deliveryAccepted.carrier.photo.isNullOrBlank()) {
            Glide.with(this).load(deliveryAccepted.carrier.photo).override(350, 0).centerCrop()
                .into(binding.imageViewCarrier)
        }
        binding.notificationPickUPReceived = true
        binding.notificationCodeReceived = true
        binding.carrierNameDescription.text = "Its on his way to the destination"
        binding.carrierPickUpCodeValue.text = deliveryAccepted?.code ?: ""

        val carrierName =
            "${deliveryAccepted.carrier.firstName} ${deliveryAccepted.carrier.lastName}"

        binding.pinLayoutDropOffPacakgeCarrier.pickUpText =
            deliveryAccepted.delivery.originAddress
        binding.pinLayoutDropOffPacakgeCarrier.dropOffText =
            deliveryAccepted.delivery.destinationAddress

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