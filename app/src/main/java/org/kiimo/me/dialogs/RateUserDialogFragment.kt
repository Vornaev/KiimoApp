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
            R.style.ThemeOverlay_MaterialComponents_Dialog_Alert
        )
        dialog.setView(binding.root)

        setListeners()

        return dialog.create()
    }

    private fun setListeners() {

    }
}