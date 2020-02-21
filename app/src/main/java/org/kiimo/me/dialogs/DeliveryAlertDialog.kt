package org.kiimo.me.dialogs

import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import org.kiimo.me.R
import org.kiimo.me.databinding.DialogDeliveryAlertBinding
import org.kiimo.me.models.Delivery
import org.kiimo.me.util.SpanUtils
import org.kiimo.me.util.StringUtils

class DeliveryAlertDialog : DialogFragment() {

    private lateinit var binding: DialogDeliveryAlertBinding

    private var delivery: Delivery? = null

    interface OnDeliveryAlertDialogListener {
        fun onDeliveryAlertDialogAccept(delivery: Delivery?)
    }

    fun setDelivery(delivery: Delivery?) {
        this.delivery = delivery
    }

    @SuppressLint("InflateParams")
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val context = context!!

        binding = DataBindingUtil.inflate(
            LayoutInflater.from(context),
            R.layout.dialog_delivery_alert,
            null,
            false
        )

        binding.layoutPin.pickUpText = getPickUpText()
        binding.layoutPin.dropOffText = getDropOffText()

        binding.priceTextView.text = getPrice()
        binding.packageSizeValueTextView.text = StringUtils.getPackageSize(context, delivery)

        val deliveryString = context.getString(R.string.dialog_delivery)
        val alertString = context.getString(R.string.dialog_alert)
        val startIndex = deliveryString.length

        val builder = AlertDialog.Builder(context)

        binding.titleTextView.text = SpanUtils.getSpannableString(
            startIndex,
            deliveryString,
            alertString
        )

        builder.setPositiveButton(
            getString(R.string.dialog_accept)
        ) { _, _ ->
            (activity as OnDeliveryAlertDialogListener).onDeliveryAlertDialogAccept(delivery)
        }

        builder.setNegativeButton(
            getString(R.string.dialog_decline)
        ) { _, _ -> }

        builder.setView(binding.root)

        return builder.create()
    }

    private fun getPrice(): String {
        delivery?.deliveryPrice?.bruttoAmount?.let {
            return it
        }

        return getString(R.string.general_error_message)
    }

    private fun getPickUpText(): String {
        delivery?.originAddress?.let {
            return it
        }

        return getString(R.string.general_error_message)
    }

    private fun getDropOffText(): String {
        delivery?.destinationAddress?.let {
            return it
        }

        return getString(R.string.general_error_message)
    }
}