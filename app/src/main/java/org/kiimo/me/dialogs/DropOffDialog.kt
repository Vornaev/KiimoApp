package org.kiimo.me.dialogs

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import kotlinx.android.synthetic.main.dialog_signature_drop_package.*
import org.kiimo.me.R
import org.kiimo.me.databinding.DialogSignatureDropPackageBinding
import org.kiimo.me.models.DeliveryPrice

class DropOffDialog : DialogFragment() {


    lateinit var binding : DialogSignatureDropPackageBinding

    interface OnDropOffDialogListener {
        fun onDropOff(signature: String)
        fun onDropOffDialogEnd(deliveryPrice: DeliveryPrice)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val context = context!!


        binding = DataBindingUtil.inflate(
            LayoutInflater.from(context),
            R.layout.dialog_signature_drop_package,
            null,
            false
        )

        val builder = AlertDialog.Builder(context)

        builder.setView(binding.root)

        return builder.create()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dialog?.setOnShowListener {
         //   val button = (dialog as AlertDialog).getButton(AlertDialog.BUTTON_POSITIVE)

            binding.btnDropOffPackageConfirm.setOnClickListener {

                binding.signaturePad.signatureBitmap
                (activity as OnDropOffDialogListener).onDropOff("https://img.deliverycoin.net/signatures/2019/11/5508abb2-0756-4827-ace7-95131b2b4986.png")
            }

//            button.setOnClickListener {
//                // todo replace with real signature
//                (activity as OnDropOffDialogListener).onDropOff("https://img.deliverycoin.net/signatures/2019/11/5508abb2-0756-4827-ace7-95131b2b4986.png")
//            }
        }

        return super.onCreateView(inflater, container, savedInstanceState)
    }

    fun dismissDialog(deliveryPrice: DeliveryPrice) {
        (activity as OnDropOffDialogListener).onDropOffDialogEnd(deliveryPrice)
        dismiss()
    }
}