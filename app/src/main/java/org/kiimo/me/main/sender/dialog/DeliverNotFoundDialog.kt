package org.kiimo.me.main.sender.dialog

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import org.kiimo.me.R
import org.kiimo.me.databinding.DialogDeliveryNotFoundBinding
import org.kiimo.me.dialogs.BaseKiimoDialog

class DeliverNotFoundDialog : BaseKiimoDialog() {

    lateinit var binding: DialogDeliveryNotFoundBinding

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        binding = DataBindingUtil.inflate(
            LayoutInflater.from(context),
            R.layout.dialog_delivery_not_found,
            null,
            false
        )

        val dialogBuilder = AlertDialog.Builder(requireContext())
        dialogBuilder.setView(binding.root)

        binding.notFoundButton.setOnClickListener {
            this.dismiss()
        }

        return dialogBuilder.create()
    }
}