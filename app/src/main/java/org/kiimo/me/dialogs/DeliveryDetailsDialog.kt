package org.kiimo.me.dialogs

import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import com.bumptech.glide.Glide
import org.kiimo.me.R
import org.kiimo.me.databinding.DialogDeliveryDetailsBinding
import org.kiimo.me.models.Delivery
import org.kiimo.me.util.StringUtils

class DeliveryDetailsDialog : BaseKiimoDialog() {

    private lateinit var binding: DialogDeliveryDetailsBinding

    private var delivery: Delivery? = null

    interface OnDeliveryDetailsDialogListener {
        fun onDeliveryDetailsDialogEnd(delivery: Delivery?)
    }

    fun setDelivery(delivery: Delivery?) {
        this.delivery = delivery
    }

    @SuppressLint("InflateParams")
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val context = context!!

        binding = DataBindingUtil.inflate(
            LayoutInflater.from(context),
            R.layout.dialog_delivery_details,
            null,
            false
        )

        setDeliveryImage()
        binding.packageSizeValueTextView.text = StringUtils.getPackageSize(context, delivery)
        binding.descriptionTextView.text = getDescription()

        val builder = AlertDialog.Builder(context)

        builder.setPositiveButton(
            getString(android.R.string.ok)
        ) { _, _ ->
            (context as OnDeliveryDetailsDialogListener).onDeliveryDetailsDialogEnd(delivery)
        }

        builder.setView(binding.root)

        return builder.create()
    }

    private fun setDeliveryImage() {
        val image = getDeliveryImage()
        if (image.isEmpty()) {
//            binding.deliveryImageView.visibility = View.GONE
        } else {
            Glide.with(this).load(image).placeholder(R.drawable.no_image_placeholder).override(500, 0).centerCrop()
                .into(binding.deliveryImageView)
        }
    }

    private fun getDeliveryImage(): String {
        delivery?.packages?.let {
            for (pack in it) {
                pack?.images?.let { images ->
                    for (image in images) {
                        image?.let {
                            return image
                        }
                    }
                }
            }
        }

        return ""
    }

    private fun getDescription(): String {
        delivery?.packages?.let {
            for (pack in it) {
                pack?.description?.let { description ->

                    if (description.isBlank()) return "No description available"
                    else return description
                }
            }
        }

        return getString(R.string.general_error_message)
    }
}