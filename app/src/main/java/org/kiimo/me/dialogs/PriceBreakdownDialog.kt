package org.kiimo.me.dialogs

import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import org.kiimo.me.R
import org.kiimo.me.databinding.DialogPriceBreakdownBinding
import org.kiimo.me.models.DeliveryPrice
import java.util.*

class PriceBreakdownDialog : BaseKiimoDialog() {

    private lateinit var binding: DialogPriceBreakdownBinding

    private lateinit var deliveryPrice: DeliveryPrice

    interface OnPriceBreakdownDialogListener {
        fun onPriceBreakdownDialogEnd()
    }

    @SuppressLint("InflateParams")
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = DataBindingUtil.inflate(
            LayoutInflater.from(activity),
            R.layout.dialog_price_breakdown,
            null,
            false
        )

        val builder = AlertDialog.Builder(activity!!)

        binding.baseFareValueTextView.text = deliveryPrice.basePrice.toString()
        binding.timeTextView.text = getTimeText()
        binding.timeValueTextView.text = getTimeValueText()
        binding.distanceTextView.text = getDistanceText()
        binding.distanceValueTextView.text = getDistanceValueText()
        binding.totalValueTextView.text = getTotalValueText()
        binding.yourTotalValueTextView.text = getYourTotalValueText()

        builder.setPositiveButton(
            getString(R.string.dialog_end)
        ) { _, _ ->
            (activity as OnPriceBreakdownDialogListener).onPriceBreakdownDialogEnd()
        }

        builder.setView(binding.root)

        return builder.create()
    }

    fun setDeliveryPrice(deliveryPrice: DeliveryPrice) {
        this.deliveryPrice = deliveryPrice
    }

    private fun getTimeText() =
        String.format(
            Locale.getDefault(),
            "- %s %.2fx%.2f",
            getString(R.string.minutes),
            deliveryPrice.minutes ?: 0.0,
            deliveryPrice.pricePerMin ?: 0.0
        )

    private fun getTimeValueText(): String {
        val minutes = deliveryPrice.minutes ?: 0.0
        val pricePerMin = deliveryPrice.pricePerMin ?: 0.0
        val price = minutes * pricePerMin
        return String.format(
            Locale.getDefault(),
            "%.2f",
            price
        )
    }

    private fun getDistanceText() =
        String.format(
            Locale.getDefault(), "- %.3f %s", deliveryPrice.kilometers ?: 0.0,
            getString(R.string.kilometers)
        )

    private fun getDistanceValueText() =
        String.format(
            Locale.getDefault(), "%d", deliveryPrice.pricePerKm ?: 0
        )

    private fun getTotalValueText() = deliveryPrice.bruttoAmount ?: ""

    private fun getYourTotalValueText() = deliveryPrice.nettoAmount ?: ""
}