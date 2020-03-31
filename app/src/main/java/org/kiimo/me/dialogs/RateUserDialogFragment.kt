package org.kiimo.me.dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import androidx.databinding.DataBindingUtil
import org.kiimo.me.R
import org.kiimo.me.databinding.DialogRateUserSenderFinishBinding

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

        dialog.setView(binding.root)


        setListeners()

        binding.ratingCount = 0

        return dialog.create()
    }

    private fun setListeners() {

        binding.rateStarOne.setOnClickListener { binding.ratingCount = 1 }
        binding.rateStarTwo.setOnClickListener { binding.ratingCount = 2 }
        binding.rateStarThree.setOnClickListener { binding.ratingCount = 3 }
        binding.rateStarFour.setOnClickListener { binding.ratingCount = 4 }
        binding.rateStarFive.setOnClickListener { binding.ratingCount = 5 }
    }
}