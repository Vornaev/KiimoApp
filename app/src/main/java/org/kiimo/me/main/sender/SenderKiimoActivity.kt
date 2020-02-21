package org.kiimo.me.main.sender

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import androidx.core.view.GravityCompat
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.google.android.gms.common.util.JsonUtils
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_kiimo_main_navigation.*
import kotlinx.android.synthetic.main.layout_menu_item.view.*
import kotlinx.android.synthetic.main.nav_header_kiimo_main_navigation.view.*
import org.kiimo.me.R
import org.kiimo.me.main.FragmentTags
import org.kiimo.me.main.MainActivity
import org.kiimo.me.main.account.ChangeAccountTypeDialog
import org.kiimo.me.main.fragments.MapFragment
import org.kiimo.me.main.fragments.MenuMyOrdersFragment
import org.kiimo.me.main.fragments.MenuPaymentTypeFragment
import org.kiimo.me.main.menu.KiimoMainNavigationActivity
import org.kiimo.me.main.sender.dialog.DeliverNotFoundDialog
import org.kiimo.me.main.sender.dialog.SendItemDescriptionDialog
import org.kiimo.me.main.sender.fragment.SenderCreateDeliveryFragment
import org.kiimo.me.main.sender.fragment.SenderDeliveryPackageSummaryFragment
import org.kiimo.me.main.sender.fragment.SenderMapFragment
import org.kiimo.me.main.sender.fragment.SenderPaymentDetailsFragment
import org.kiimo.me.main.sender.model.notifications.ConfirmPickUpNotification.ConfirmPickUpFcmData
import org.kiimo.me.main.sender.model.notifications.FirebaseSenderResponse
import org.kiimo.me.main.sender.model.request.pay.Carrier
import org.kiimo.me.main.sender.model.request.pay.PayResponse
import org.kiimo.me.models.Delivery
import org.kiimo.me.models.DeliveryPaid
import org.kiimo.me.models.FirebasePayload
import org.kiimo.me.util.AppConstants
import org.kiimo.me.util.JsonUtil
import org.kiimo.me.util.PreferenceUtils
import timber.log.Timber
import java.util.*

class SenderKiimoActivity : KiimoMainNavigationActivity() {


    var dialogSpiner: SendItemDescriptionDialog? = null
    var mapFragment: SenderMapFragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mapFragment = SenderMapFragment()
        if (savedInstanceState == null) replaceFragment(mapFragment!!)

        setObservers()
        setupHeaderView()

        handlePayoload(intent)
    }

    fun setupHeaderView() {

        nav_view?.getHeaderView(0)?.buttonChangeAccountTypeUser?.setOnClickListener {
            val userProfile = PreferenceUtils.getUserParsed(this)
            if (userProfile.address.street.isEmpty()) {
                val dialog = ChangeAccountTypeDialog()
                dialog.setUser(userProfile, viewModel)
                dialog.show(supportFragmentManager, "changeAccountDialog")
            } else {
                openDeliver()
            }
        }
    }

    fun setObservers() {
        viewModel.createDelPackageCreateLData.observe(this, Observer {
            Timber.i("response create delivery", it)

            popToRootMapView()
            Handler().postDelayed(Runnable {
                dialogSpiner = SendItemDescriptionDialog()
                dialogSpiner?.show(
                    supportFragmentManager,
                    AppConstants.DIALOG_SENDER_DESCRIPTION_ITEM
                )
                dialogSpiner?.animateImage()
            }, 50)
        })


        viewModel.payPackageLiveData.observe(this, Observer {
            onPayReceived(it)
        })


        viewModel.updateUserProfileLiveData.observe(
            this, Observer {
                openDeliver()
            })
    }


    fun shouldPopToMap() {
        if (supportFragmentManager.fragments.size > 0) {
            if (supportFragmentManager.fragments.last() is SenderMapFragment) {
                return
            } else {
                popToRootMapView()
            }
        }
    }

    fun popToRootMapView() {
        supportFragmentManager.popBackStack(AppConstants.MAP_ROOT_FRAGMENT, 0)
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

    fun openDeliver() {
        PreferenceUtils.saveAccountType(this, false)
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    fun openCreateDeliveryItemDialog() {
        addFragment(SenderCreateDeliveryFragment())
    }

    fun openPackageDetailsFragment() {
        addFragment(SenderDeliveryPackageSummaryFragment())
    }


    override fun setupDeliveriesFragment() {
        menuItemMyDelivery.textViewMenuItem.text = "My orders"
        menuItemMyDelivery.imgMenuItemImage.setImageDrawable(getDrawable(R.drawable.ic_menu_my_deliveries))

        menuItemMyDelivery.setOnClickListener {
            openMenuFragment(
                MenuMyOrdersFragment::class.java,
                FragmentTags.MENU_MY_ORDERS
            )
            closeMenuDrawer()
        }

    }

    override fun setupPaymentMenuFragment() {
        menuItemMyPayment.textViewMenuItem.text = getString(R.string.payment_menu_item)
        menuItemMyPayment.imgMenuItemImage.setImageDrawable(getDrawable(R.drawable.ic_menu_payment))
        menuItemMyPayment.setOnClickListener {
            openMenuFragment(
                MenuPaymentTypeFragment::class.java,
                FragmentTags.MENU_PAYMENT_TYPE
            )
            closeMenuDrawer()
        }
    }


    private val messageReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent) {
            handlePayoload(intent)
        }
    }

    fun handlePayoload(newIntent: Intent) {

        shouldPopToMap()

        val payloadString = newIntent.extras?.getString(AppConstants.FIREBASE_PAYLOAD)
        if (payloadString.isNullOrEmpty()) return

        val payload = Gson().fromJson(payloadString, FirebasePayload::class.java)
        val deliveryAccepted =
            Gson().fromJson(payload.delivery, FirebaseSenderResponse::class.java)

        when (payload.type) {
            "DELIVERY_ACCEPTED" -> {
                dialogSpiner?.dismiss()
                addFragment(SenderPaymentDetailsFragment())
            }

            "DELIVERY_PICKED_UP" -> {
                val pickUp = JsonUtil.loadModelFromJson<ConfirmPickUpFcmData>(payload.delivery)
                mapFragment!!.receviedCodePickUP(pickUp)
            }

            "DELIVERY_DROP_OFF" -> {
                mapFragment!!.finishedDelivery()
            }

            "CARRIER_NOT_FOUND" -> {
                showTryAgainDialog()
            }

        }
    }

    private fun onPayReceived(payResponse: PayResponse) {
        popToRootMapView()
        mapFragment?.receivedNotificationPickUp(payResponse)
    }

    private fun showTryAgainDialog() {
        dialogSpiner?.dismiss()
        var tryAgainDialog = DeliverNotFoundDialog()
        tryAgainDialog.show(supportFragmentManager, "tryAgainDialog")
    }
}
