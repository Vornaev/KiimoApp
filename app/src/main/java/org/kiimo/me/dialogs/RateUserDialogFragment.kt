package org.kiimo.me.dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.inputmethod.InputMethodManager
import androidx.databinding.DataBindingUtil
import org.kiimo.me.R
import org.kiimo.me.databinding.DialogRateUserSenderFinishBinding
import org.kiimo.me.main.sender.SenderKiimoActivity

class RateUserDialogFragment : BaseKiimoDialog() {

    lateinit var binding: DialogRateUserSenderFinishBinding


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

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

        binding.ratingCount = 0

        return dialog.create()
    }

    private fun setListeners() {

        binding.topDialogRateLayout.setOnClickListener {
            if (binding.rateUserComment.hasFocus()) {
                val imm = binding.rateUserComment.context
                    .getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(binding.rateUserComment.windowToken, 0)
            }
        }

        binding.rateStarOne.setOnClickListener { binding.ratingCount = 1 }
        binding.rateStarTwo.setOnClickListener { binding.ratingCount = 2 }
        binding.rateStarThree.setOnClickListener { binding.ratingCount = 3 }
        binding.rateStarFour.setOnClickListener { binding.ratingCount = 4 }
        binding.rateStarFive.setOnClickListener { binding.ratingCount = 5 }

        binding.ratingButtonConfirm.setOnClickListener {
            (requireActivity() as SenderKiimoActivity).onDissmissDialogRateUser()
        }
    }
}