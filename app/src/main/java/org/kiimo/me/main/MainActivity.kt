package org.kiimo.me.main

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.os.Handler
import androidx.lifecycle.Observer
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.google.android.gms.maps.model.LatLng
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_kiimo_main_navigation.*
import kotlinx.android.synthetic.main.nav_header_kiimo_main_navigation.view.*
import org.kiimo.me.R
import org.kiimo.me.dialogs.*
import org.kiimo.me.main.fragments.MapFragment
import org.kiimo.me.main.menu.KiimoMainNavigationActivity
import org.kiimo.me.main.sender.SenderKiimoActivity
import org.kiimo.me.models.*
import org.kiimo.me.util.AppConstants
import org.kiimo.me.util.DialogUtils
import org.kiimo.me.util.PreferenceUtils

class MainActivity : KiimoMainNavigationActivity(),
    DeliveryAlertDialog.OnDeliveryAlertDialogListener,
    DeliveryDetailsDialog.OnDeliveryDetailsDialogListener,
    DigitCodeDialog.OnDigitCodeDialogListener,
    PriceBreakdownDialog.OnPriceBreakdownDialogListener,
    DropOffDialog.OnDropOffDialogListener {

    private lateinit var mainActivityInterface: MainActivityInterface

    private lateinit var digitCodeDialog: DigitCodeDialog
    private lateinit var dropOffDialog: DropOffDialog

    interface MainActivityInterface {
        fun acceptDelivery(deliveryId: String)
        fun onOriginReady(origin: LatLng, originAddress: String, destinationAddress: String)
        fun onOriginDestinationReady(deliveryPaid: DeliveryPaid)
        fun onDeliveryReady(delivery: Delivery?)
        fun validateCode(code: String)
        fun onDropOff(signature: String)
        fun deliveryFinished()
    }

    fun setOnOriginDestinationReady(mainActivityInterface: MainActivityInterface) {
        this.mainActivityInterface = mainActivityInterface
    }

    override fun onDeliveryAlertDialogAccept(delivery: Delivery?) {
        delivery?.deliveryId?.let {
            mainActivityInterface.acceptDelivery(it)
        }

        showDeliveryDetailsDialog(delivery)
    }

    override fun onDeliveryDetailsDialogEnd(delivery: Delivery?) {
        mainActivityInterface.onDeliveryReady(delivery)
        if (isDeliveryValidData(delivery)) {
            mainActivityInterface.onOriginReady(
                getOrigin(delivery),
                getOriginAddress(delivery),
                getDestinationAddress(delivery)
            )
        }
    }


    override fun onValidateCode(code: String) {
        mainActivityInterface.validateCode(code)
    }

    override fun onDigitCodeDialogEnd() {
        showDropOffDialog()
    }

    override fun onDropOff(signature: String) {
        mainActivityInterface.onDropOff(signature)
    }

    override fun onDropOffDialogEnd(deliveryPrice: DeliveryPrice) {
        showPriceBreakdownDialog(deliveryPrice)
    }

    override fun onPriceBreakdownDialogEnd() {
        mainActivityInterface.deliveryFinished()
    }

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (savedInstanceState == null) replaceFragment(MapFragment.newInstance())
        putDeliveryType()


        nav_view?.getHeaderView(0)?.buttonChangeAccountTypeUser?.setOnClickListener {
            PreferenceUtils.saveAccountType(this, true)
            viewModel.putStatus(Status(false))
            startActivity(Intent(this, SenderKiimoActivity::class.java))
        }

        nav_view?.getHeaderView(0)?.buttonChangeAccountTypeUser?.text =
            getString(R.string.i_want_to_send_button)

        Handler().postDelayed(Runnable { handlePayload(intent) }, 1000)

        viewModel.signatureLiveData.observe(
            this, Observer {
                val signatureDefault = "https://img.deliverycoin.net/signatures/2019/11/5508abb2-0756-4827-ace7-95131b2b4986.png"
                onDropOff(it.imageUrl)
            }
        )
    }

    override fun onStart() {
        super.onStart()
        LocalBroadcastManager.getInstance(this).registerReceiver(
            (messageReceiver),
            IntentFilter(AppConstants.FIREBASE_BROADCAST)
        )
    }

    override fun onStop() {
        super.onStop()
        LocalBroadcastManager.getInstance(this).unregisterReceiver(messageReceiver)
    }

    private fun showDeliveryAlertDialog(delivery: Delivery?) {
        val deliveryAlertDialog = DeliveryAlertDialog()
        deliveryAlertDialog.setDelivery(delivery)
        deliveryAlertDialog.show(supportFragmentManager, AppConstants.DIALOG_DELIVERY_ALERT_TAG)
    }

    private fun showDeliveryDetailsDialog(delivery: Delivery?) {
        val deliveryDetailsDialog = DeliveryDetailsDialog()
        deliveryDetailsDialog.setDelivery(delivery)
        deliveryDetailsDialog.show(supportFragmentManager, AppConstants.DIALOG_DELIVERY_DETAILS_TAG)
    }

    fun showDigitCodeDialog() {
        digitCodeDialog = DigitCodeDialog()
        digitCodeDialog.show(supportFragmentManager, AppConstants.DIALOG_DIGIT_CODE_TAG)
    }

    fun dismissDigitCodeDialog() {
        digitCodeDialog.dismissDialog()
    }

    fun showDigitCodeDialogValidationError() {
        digitCodeDialog.showDigitCodeDialogValidationError()
    }

    private fun showDropOffDialog() {
        dropOffDialog = DropOffDialog()
        dropOffDialog.show(supportFragmentManager, AppConstants.DIALOG_PRICE_BREAKDOWN_TAG)
    }

    fun dismissDropOffDialog(deliveryPrice: DeliveryPrice) {
        dropOffDialog.dismissDialog(deliveryPrice)
    }

    private fun showPriceBreakdownDialog(deliveryPrice: DeliveryPrice) {
        val priceBreakdownDialog = PriceBreakdownDialog()
        priceBreakdownDialog.setDeliveryPrice(deliveryPrice)
        priceBreakdownDialog.show(supportFragmentManager, AppConstants.DIALOG_PRICE_BREAKDOWN_TAG)
    }

    private val messageReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent) {
            handlePayload(intent)
        }
    }

    override fun handlePayload(notIntent: Intent) {
        val payloadString = notIntent.extras?.getString(AppConstants.FIREBASE_PAYLOAD)
        if (payloadString.isNullOrEmpty()) return
        val payload = Gson().fromJson(payloadString, FirebasePayload::class.java)

        if ("DELIVERY_PAID" == payload.type) {
            val deliveryPaid = Gson().fromJson(payload.delivery, DeliveryPaid::class.java)
            if (isDeliveryValidData(deliveryPaid.delivery)) {
                mainActivityInterface.onOriginDestinationReady(
                    deliveryPaid
                )
            }
        } else if ("DELIVERY_REQUEST" == payload.type) {
            val delivery = Gson().fromJson(payload.delivery, Delivery::class.java)
            showDeliveryAlertDialog(delivery)
        } else {
            // DialogUtils.showErrorMessage(this,"error notification")
            val typy = payload.type
        }
    }


    private fun isDeliveryValidData(delivery: Delivery?): Boolean {
        val originLat = delivery?.origin?.lat
        val originLng = delivery?.origin?.lng
        val destinationLat = delivery?.destination?.lat
        val destinationLng = delivery?.destination?.lng

        return originLat != null && originLng != null && destinationLat != null && destinationLng != null
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

    override fun onBackPressed() {
        super.onBackPressed()
    }

    private fun getOriginAddress(delivery: Delivery?) = delivery?.originAddress ?: ""

    private fun getDestinationAddress(delivery: Delivery?) = delivery?.destinationAddress ?: ""
}
