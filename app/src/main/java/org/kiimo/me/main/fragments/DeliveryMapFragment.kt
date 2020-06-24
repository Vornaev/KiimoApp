package org.kiimo.me.main.fragments

import android.Manifest
import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Context.LOCATION_SERVICE
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.crashlytics.android.Crashlytics
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.AutocompleteActivity
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.kiimo.me.R
import org.kiimo.me.app.BaseMainFragment
import org.kiimo.me.databinding.FragmentMapBinding
import org.kiimo.me.main.FragmentTags
import org.kiimo.me.main.MainActivity
import org.kiimo.me.main.components.DaggerMainComponent
import org.kiimo.me.main.modules.MapModule
import org.kiimo.me.main.viewmodels.MapViewModel
import org.kiimo.me.main.viewmodels.MapViewModelFactory
import org.kiimo.me.models.*
import org.kiimo.me.services.LocationServicesKiimo
import org.kiimo.me.util.*
import java.io.ByteArrayOutputStream
import java.util.*
import javax.inject.Inject

class DeliveryMapFragment : BaseMainFragment(), OnMapReadyCallback, GoogleMap.OnMapClickListener,
    LocationListener, MainActivity.MainActivityInterface {


    val userToken: String by lazy { PreferenceUtils.getUserToken(requireContext()) }

    @Inject
    lateinit var mapViewModelFactory: MapViewModelFactory

    private val mapViewModel: MapViewModel by viewModels {
        mapViewModelFactory
    }

    private lateinit var binding: FragmentMapBinding

    private var isPickUp = false
    private var googleMap: GoogleMap? = null

    private val markerPoints = arrayListOf<LatLng>()
    private var origin: LatLng? = null
    private var destination: LatLng? = null
    private var myLocation: LatLng? = null

    private var userLocation: Location? = null

    private var delivery: Delivery? = null

    private var markers = arrayListOf<Marker>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMapBinding.inflate(inflater, container, false)

        DaggerMainComponent.builder().mapModule(MapModule()).build().inject(this)

        requestAccessFineLocationPermission()

        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(this)

        setListeners()
        setObservers()

        mapViewModel.getSelf(userToken)

        setEarnings()

        binding.travelModeActiveId = 0

        return binding.root
    }

    private fun setEarnings() {
        mainDeliveryViewModel().deliveryListLiveData.observe(
            viewLifecycleOwner,
            androidx.lifecycle.Observer {
                val result = it.sumByDouble { la -> la.price }
                binding.earningsButton.text = String.format("%.1f MKD", result)
            })

        mainDeliveryViewModel().getDeliveryList()

        binding.earningsButton.setOnClickListener {
            (requireActivity() as MainActivity).openMenuFragment(
                MenuMyDeliveriesFragment::class.java,
                FragmentTags.MENU_PAYMENT_TYPE
            )
        }
    }

    private fun loadFromPreference() {
        val userProf = PreferenceUtils.getUserParsed(requireContext())
        userProf?.let {

            if (it.photo.isNotBlank()) {
                loadProfileImage(it.photo)
            }
        }
    }

    private fun loadProfileImage(imageUrl: String) {
        if (imageUrl.isBlank()) return

        Glide.with(this).load(imageUrl).placeholder(R.drawable.ic_user_profile)
            .apply(RequestOptions.circleCropTransform().override(0, 350))
            .into(binding.imageViewProfileDrawer)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        val width = 700
        val height = 900

        if (requestCode == MediaManager.REQUEST_IMAGE_CAPTURE) {
            MediaManager.getBitmap(
                width.toFloat(), height.toFloat()
            )?.apply {
                uploadBitmap(this)
            }
        }

        if (requestCode == AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                data?.let {
                    val place = Autocomplete.getPlaceFromIntent(it)
                    if (isPickUp) {
                        binding.layoutPin.pickUpText = place.address
                    } else {
                        binding.layoutPin.dropOffText = place.address
                    }

                    place.latLng?.apply {
                        addMarker(this)
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

        mainDeliveryViewModel().uploadPhotoForPackage(body)

        stream.flush()
        stream.close()
    }

    override fun onMapReady(googleMap: GoogleMap) {
        this.googleMap = googleMap
        setMyLocationEnabled()
    }

    override fun onMapClick(latLng: LatLng?) {
        latLng ?: return
        addMarker(latLng)
    }


    override fun onLocationChanged(location: Location?) {
        location?.let {
            myLocation = LatLng(it.latitude, it.longitude)
        }

        if (location != null) {
            googleMap?.moveCamera(
                CameraUpdateFactory.newLatLngZoom(
                    myLocation, GOOGLE_MAP_ZOOM
                )
            )

        }
    }

    fun onDeliveryTypeLoaded(deliveryTypeId: String) {
        when (deliveryTypeId) {
            DeliveryTypeID.FOOT -> binding.travelModeActiveId = 0
            DeliveryTypeID.BIKE -> binding.travelModeActiveId = 1
            DeliveryTypeID.SCOOTER -> binding.travelModeActiveId = 2
            DeliveryTypeID.CAR -> binding.travelModeActiveId = 3
        }
    }

    override fun onStatusChanged(p0: String?, p1: Int, p2: Bundle?) {
    }

    override fun onProviderEnabled(p0: String?) {
        //   DialogUtils.showSuccessMessage(requireActivity(), "Provider enabled")
    }

    override fun onProviderDisabled(p0: String?) {
    }

    override fun onOriginDestinationReady(deliveryPaid: DeliveryPaid) {
        binding.isShowMapRoute = true
        binding.isConfirmDropOff = false
        setPinText(
            deliveryPaid.delivery.originAddress ?: "",
            deliveryPaid.delivery.destinationAddress ?: ""
        )
        markerPoints.clear()
        addMarker(getOrigin(deliveryPaid.delivery))
        addMarker(getDestination(deliveryPaid.delivery))
        this.delivery = deliveryPaid.delivery

        binding.navigateButton.setOnClickListener {

            if (mapViewModel.pickUpImageUrl.isBlank()) {
                DialogUtils.showErrorMessage(
                    requireActivity(),
                    getString(R.string.error_missing_image)
                )
            } else {
                LocationServicesKiimo.openMapsDirectionsActivity(
                    requireContext()
                    , deliveryPaid.delivery.destination?.lat!!
                    , deliveryPaid.delivery.destination?.lng!!
                    , travelMode = getTravelModeMaps()
                )
            }
            validatePickUPButton()
        }

        binding.navigatePackageImage.setOnClickListener {
            MediaManager.getDispatchTakePictureIntent(this)
        }
    }

    fun getTravelModeMaps(): String {
        return when (binding.travelModeActiveId) {
            0 -> LocationServicesKiimo.TravelModes.walking
            1 -> LocationServicesKiimo.TravelModes.bicycle
            2 -> LocationServicesKiimo.TravelModes.scooter
            3 -> LocationServicesKiimo.TravelModes.drive
            else -> LocationServicesKiimo.TravelModes.drive
        }
    }

    override fun acceptDelivery(deliveryId: String) {
        mapViewModel.acceptDelivery(userToken, deliveryId)
    }

    override fun onOriginReady(
        origin: LatLng,
        originAddress: String,
        destinationAddress: String
    ) {
        addMarker(origin)
        setPinText(originAddress, destinationAddress)
    }

    override fun onDeliveryReady(delivery: Delivery?) {
        this.delivery = delivery
    }

    override fun validateCode(code: String) {
        mapViewModel.validateCode(
            userToken,
            ValidateCodeRequest(code)
        )
    }

    override fun onDropOff(signature: String) {
        mapViewModel.dropOff(
            userToken,
            DropOffRequest(signature)
        )
    }

    override fun deliveryFinished() {
        binding.isShowMapRoute = false
        binding.isConfirmDropOff = false
        for (marker in markers) {
            marker.remove()
        }
        mapViewModel.pickUpImageUrl = ""
        markers.clear()
        googleMap?.clear()
    }

    override fun onEarningReady(value: Float) {
        mainDeliveryViewModel().getDeliveryList()
    }

    private fun setListeners() {
//        binding.layoutPin.pinPickUpConstraintLayout.setOnClickListener {
//            isPickUp = true
//            sendRequest()
//        }
//        binding.layoutPin.pinDropOffConstraintLayout.setOnClickListener {
//            isPickUp = false
//            sendRequest()
//        }
        binding.footImageView.setOnClickListener {
            binding.travelModeActiveId = 0
            mainDeliveryViewModel().putDeliveryType(deliveryType = DeliveryType(DeliveryTypeID.FOOT))
        }
        binding.bicycleImageView.setOnClickListener {
            binding.travelModeActiveId = 1
            mainDeliveryViewModel().putDeliveryType(deliveryType = DeliveryType(DeliveryTypeID.BIKE))
        }
        binding.scooterImageView.setOnClickListener {


            binding.travelModeActiveId = 2
            mainDeliveryViewModel().putDeliveryType(deliveryType = DeliveryType(DeliveryTypeID.SCOOTER))
        }
        binding.carImageView.setOnClickListener {
            binding.travelModeActiveId = 3
            mainDeliveryViewModel().putDeliveryType(deliveryType = DeliveryType(DeliveryTypeID.CAR))
        }

        binding.imageViewProfileDrawer.setOnClickListener {

            getNavigationActivity()?.onOpenDrawerClicked()
        }

        binding.confirmButton.setOnClickListener {
            when {
                binding.isConfirmDropOff -> {
                    (activity as MainActivity).showDigitCodeDialog()
                }
                else -> {
                    when {
                        mapViewModel.pickUpImageUrl.isBlank() -> {
                            DialogUtils.showErrorMessage(
                                requireActivity(),
                                getString(R.string.error_missing_image)
                            )
                        }
                        else -> {
                            mapViewModel.pickUp(userToken)
                        }
                    }
                    validatePickUPButton()
                }
            }
        }
        binding.goOnlineImageView.setOnClickListener {
            binding.isOnline = true
            getMainActivity()!!.viewModel.isValidDeliverer()
        }

        binding.isOnlineStatusImageView.setOnClickListener {
            binding.isOnline = false
            mapViewModel.putStatus(
                token = userToken,
                status = Status(false)
            )
        }

        binding.myLocationImageView.setOnClickListener {
            if (hasLocationPermission()) {
                LocationServicesKiimo.getUserDeviceLocation(
                    requireContext(),
                    ::onSucesssGetLocation
                )
            } else {
                requestAccessFineLocationPermission()
            }
        }
        (activity as MainActivity).setOnOriginDestinationReady(this)
    }

    private fun validatePickUPButton() {
        binding.navigatePackageImage.setTextColor(
            ContextCompat.getColor(
                requireContext(),
                if (mapViewModel.pickUpImageUrl.isBlank()) R.color.colorValidation else R.color.quantum_black_100
            )
        )
    }


    private fun onSucesssGetLocation(location: Location) {
        onLocationChanged(location)
    }

    private fun sendRequest() {
        // Start the autocomplete intent.
        val intent = Autocomplete.IntentBuilder(
            AutocompleteActivityMode.FULLSCREEN, listOf(
                Place.Field.ID, Place.Field.NAME, Place.Field.ADDRESS,
                Place.Field.LAT_LNG
            )
        ).build(activity!!)
        startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE)
    }

    private fun addMarker(latLng: LatLng) {
        val googleMap = googleMap ?: return
        if (markerPoints.size > 1) {
            markerPoints.clear()
            googleMap.clear()
        }

        // Adding new item to the ArrayList
        markerPoints.add(latLng)

        // Creating MarkerOptions
        val options = MarkerOptions()

        // Setting the position of the markerList
        options.position(latLng)

        /**
         * For the start locationModel, the color of markerList is GREEN and
         * for the end locationModel, the color of markerList is RED.
         */

        val context = context
        context?.apply {
            if (markerPoints.size == 1) {
                options.icon(bitmapDescriptorFromVector(this, R.drawable.ic_pin_pick_up))
                    .title("pick up")

            } else if (markerPoints.size == 2) {
                options.icon(bitmapDescriptorFromVector(this, R.drawable.ic_pin_drop_off))
                    .title("drop off")
            }
        }

        // Add new markerList to the Google Map Android API V2
        markers.add(googleMap.addMarker(options))

        // Checks, whether start and end locations are captured
        if (markerPoints.size >= 2) {
            origin = markerPoints[0]
            destination = markerPoints[1]
        }
    }

    private fun bitmapDescriptorFromVector(
        context: Context,
        @DrawableRes vectorDrawableResourceId: Int
    ): BitmapDescriptor? {
        val vectorDrawable = ContextCompat.getDrawable(context, vectorDrawableResourceId)
        vectorDrawable?.setBounds(
            0,
            0,
            vectorDrawable.intrinsicWidth / 2,
            vectorDrawable.intrinsicHeight / 2
        )
        if (vectorDrawable == null) return null
        val bitmap = Bitmap.createBitmap(
            vectorDrawable.intrinsicWidth / 2,
            vectorDrawable.intrinsicHeight / 2,
            Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)
        vectorDrawable.draw(canvas)
        return BitmapDescriptorFactory.fromBitmap(bitmap)
    }

    private fun drawRoute(origin: LatLng, destination: LatLng) {
        // Getting URL to the Google Directions API
        val context = context ?: return

        mapViewModel.getDestinationData(
            StringUtils.getDirectionsUrl(
                context,
                origin,
                destination,
                getTravelMode()
            )
        )
    }

    private fun getTravelMode(): String =
        when (binding.travelModeActiveId) {
            1 -> "bicycling"
            2 -> "bicycling"
            3 -> "driving"
            else -> "walking"
        }


    override fun onPermissionLocationEnabled() {
        setMyLocationEnabled()
    }

    fun setMyLocationEnabled() {
        val activity = activity
        activity?.let {
            if (ContextCompat.checkSelfPermission(
                    activity,
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
                == PackageManager.PERMISSION_GRANTED
            ) {
                googleMap?.isMyLocationEnabled = true
                googleMap?.uiSettings?.isMapToolbarEnabled = true
                googleMap?.uiSettings?.isMyLocationButtonEnabled = false

                val locationManager = activity.getSystemService(LOCATION_SERVICE) as LocationManager
                locationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER,
                    5000L,
                    10f,
                    this
                )


                val fusedLocationClient =
                    LocationServices.getFusedLocationProviderClient(requireContext())
                fusedLocationClient.lastLocation.addOnSuccessListener {
                    if (it != null) {
                        userLocation = it
                        onLocationChanged(it)

                        mapViewModel.putLocation(
                            userToken,
                            locationModel = LocationModel(it.latitude, it.longitude)
                        )
                    }
                }
            }
        }
    }


    private fun setObservers() {
        mapViewModel.destinationData.observe(viewLifecycleOwner) {
            if (it == null) {
                errorHandle()
                return@observe
            }

            parseData(it)
        }
        mapViewModel.exception.observe(viewLifecycleOwner) { throwable ->
            throwable?.apply {
                Crashlytics.logException(this)
                // Toast.makeText(activity, this.localizedMessage, Toast.LENGTH_SHORT).show()
            }
        }
        mapViewModel.selfLiveData.observe(viewLifecycleOwner) { self ->
            binding.isOnline = self?.userStatus?.online == true

            if (self?.userStatus?.deliveryTypeId == null) {
                mainDeliveryViewModel().putDeliveryType()
                onDeliveryTypeLoaded(DeliveryTypeID.FOOT)
            }

            self?.userStatus?.deliveryTypeId?.let {
                onDeliveryTypeLoaded(it)
            }

            mainDeliveryViewModel().isValidDeliverer()
        }


        mapViewModel.statusResponseLiveData.observe(viewLifecycleOwner) {
            binding.isOnline = it?.online == true

//            if (it?.online == true) {
//                mapViewModel.putLocation(
//                    userToken,
//                    LocationModel(userLocation!!.latitude, userLocation!!.longitude)
//                )
//            }
        }
        mapViewModel.pickUpLiveData.observe(viewLifecycleOwner) {
            binding.isConfirmDropOff = true
            val origin = origin
            val destination = destination
            if (origin != null && destination != null) {
                drawRoute(origin, destination)
            }
        }
        mapViewModel.validateCodeLiveData.observe(viewLifecycleOwner) {
            if (it == true) {
                (activity as MainActivity).dismissDigitCodeDialog()
            } else {
                (activity as MainActivity).showDigitCodeDialogValidationError()
            }
        }
        mapViewModel.dropOffLiveData.observe(viewLifecycleOwner) {
            it?.deliveryPrice?.apply {
                (activity as MainActivity).dismissDropOffDialog(this)
            }
        }

        getMainActivity()!!.viewModel.isValidDelivererLiveData.observe(
            viewLifecycleOwner, androidx.lifecycle.Observer {
                if (it.status == 200) {

                    mapViewModel.putStatus(
                        userToken,
                        Status(binding.isOnline)
                    )

                }
            }
        )

        mainDeliveryViewModel().userProfileLiveData.observe(
            viewLifecycleOwner,
            androidx.lifecycle.Observer {
                loadProfileImage(it.photo)
            })

        mainDeliveryViewModel().photoProfileLiveData.observe(
            viewLifecycleOwner,
            androidx.lifecycle.Observer {
                loadProfileImage(it.imageUrl)
            })


        mainDeliveryViewModel().photoPackageLiveData.observe(
            viewLifecycleOwner,
            androidx.lifecycle.Observer {
                mapViewModel.pickUpImageUrl = it.imageUrl
                binding.navigatePackageImage.visibility = View.GONE
            })
    }

    private fun errorHandle() {
        if (binding.travelModeActiveId != 3) {
            Toast.makeText(
                activity,
                String.format(
                    Locale.getDefault(),
                    "%s %s - %s",
                    getString(R.string.no_data_for),
                    getTravelMode(),
                    getString(R.string.reset_to_default)
                ),
                Toast.LENGTH_SHORT
            ).show()
            binding.travelModeActiveId = 3

            val origin = origin
            val destination = destination
            if (origin != null && destination != null) {
                drawRoute(origin, destination)
            }
        } else {
            Toast.makeText(
                activity,
                String.format(
                    Locale.getDefault(),
                    "%s %s",
                    getString(R.string.no_data_for),
                    getTravelMode()
                ),
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun parseData(destinationData: DestinationData) {
        val points = arrayListOf<LatLng>()
        val polylineOptions = PolylineOptions()

        destinationData.routes?.apply {
            for (route in this) {
                route?.legs?.apply {
                    for (leg in this) {
                        leg?.steps?.apply {
                            for (step in this) {
                                if (step?.startLocationModel?.lat != null
                                    && step.startLocationModel.lng != null
                                    && step.endLocationModel?.lat != null
                                    && step.endLocationModel.lng != null
                                ) {
                                    points.add(
                                        LatLng(
                                            step.startLocationModel.lat,
                                            step.startLocationModel.lng
                                        )
                                    )
                                    points.add(
                                        LatLng(
                                            step.endLocationModel.lat,
                                            step.endLocationModel.lng
                                        )
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        if (points.isEmpty()) {
            errorHandle()
            return
        }
        // Adding all the points in the route to LineOptions
        polylineOptions.addAll(points)
        polylineOptions.width(8f)
        polylineOptions.color(Color.RED)

        setPinText(destinationData)

        googleMap?.addPolyline(polylineOptions)
    }

    private fun setPinText(destinationData: DestinationData) {
        if (destinationData.routes?.isNotEmpty() == true && destinationData.routes[0]?.legs?.isNotEmpty() == true) {
            val leg = destinationData.routes[0]?.legs?.get(0)
            leg?.apply {
                binding.layoutPin.pickUpText = leg.startAddress ?: ""
                binding.layoutPin.dropOffText = leg.endAddress ?: ""
            }
        }
    }

    private fun getOrigin(delivery: Delivery?): LatLng {
        val lat = delivery?.origin?.lat
        val lng = delivery?.origin?.lng

        return LatLng(lat!!, lng!!)
    }

    private fun getDestination(delivery: Delivery?): LatLng {
        val lat = delivery?.destination?.lat
        val lng = delivery?.destination?.lng

        return LatLng(lat!!, lng!!)
    }

    private fun setPinText(originAddress: String, destinationAddress: String) {
        binding.layoutPin.pickUpText = originAddress
        binding.layoutPin.dropOffText = destinationAddress
    }

    companion object {
        @JvmStatic
        fun newInstance(): DeliveryMapFragment = DeliveryMapFragment()

        const val MY_LOCATION_REQUEST_CODE = 987
        const val AUTOCOMPLETE_REQUEST_CODE = 988

        const val GOOGLE_MAP_ZOOM = 15f
    }
}