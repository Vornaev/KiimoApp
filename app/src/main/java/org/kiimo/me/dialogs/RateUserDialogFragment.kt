package org.kiimo.me.dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.inputmethod.InputMethodManager
import androidx.databinding.DataBindingUtil
import com.squareup.picasso.Picasso
import org.kiimo.me.R
import org.kiimo.me.databinding.DialogRateUserSenderFinishBinding
import org.kiimo.me.main.sender.SenderKiimoActivity
import org.kiimo.me.main.sender.model.notifications.dropOffDeliverySender.FcmResponseOnDropOffDelivery
import org.kiimo.me.main.sender.model.request.rate.RateDeliveryRequest
import java.lang.Exception

class RateUserDialogFragment(val data: FcmResponseOnDropOffDelivery) : BaseKiimoDialog() {

    lateinit var binding: DialogRateUserSenderFinishBinding
    var dropOffDelivery: FcmResponseOnDropOffDelivery? = null


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        dropOffDelivery = data

        binding = DataBindingUtil.inflate(
            LayoutInflater.from(context),
            R.layout.dialog_rate_user_sender_finish,
            null,
            false
        )

        val dialog = AlertDialog.Builder(
            requireContext(),
            R.style.AppCompatAlertDialogStyle
        )
//        val dialog2 = Dialog(requireContext(), R.style.AppCompatAlertDialogStyle)
//
//        dialog2.setContentView(binding.root)
//        dialog2.window?.setBackgroundDrawable(
//            ContextCompat.getDrawable(
//                requireContext(),
//                R.drawable.cornered_dialog_bacground
//            )
//        )

        dialog.setView(binding.root)


        setListeners()
        setValue(data)

        binding.ratingCount = 5

        return dialog.create()
    }

    private fun setListeners() {

        binding.topDialogRateLayout.setOnClickListener {
            if (binding.rateUserComment.hasFocus()) {
                try {
                    val imm = binding.rateUserComment.context
                        .getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(binding.rateUserComment.windowToken, 0)
                } catch (ex: Exception) {

                }
            }
        }

        binding.rateStarOne.setOnClickListener { binding.ratingCount = 1 }
        binding.rateStarTwo.setOnClickListener { binding.ratingCount = 2 }
        binding.rateStarThree.setOnClickListener { binding.ratingCount = 3 }
        binding.rateStarFour.setOnClickListener { binding.ratingCount = 4 }
        binding.rateStarFive.setOnClickListener { binding.ratingCount = 5 }



        binding.ratingButtonConfirm.setOnClickListener {


            (requireActivity() as SenderKiimoActivity).viewModel.rateUserForDelivery(
                RateDeliveryRequest(binding.ratingCount!!)
            )


            Handler().postDelayed(Runnable {

                this.dismiss()

                startActivity(
                    Intent(
                        requireContext(),
                        SenderKiimoActivity::class.java
                    )
                )
            }, 1500)

            //(requireActivity() as SenderKiimoActivity).onDissmissDialogRateUser()
        }
    }

    fun setValue(thisRef: FcmResponseOnDropOffDelivery) {
        this.dropOffDelivery = thisRef
        binding.rateUserAvatarImage
        if (thisRef.carrier.photo.isNotBlank()) {
            Picasso.get().load(thisRef.carrier.photo).fit().centerCrop()
                .into(binding.rateUserAvatarImage)
        }

        val name = "${thisRef.carrier.firstName} ${thisRef.carrier.lastName}"
        binding.titleRateUserName.text = name
    }
}