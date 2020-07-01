package org.kiimo.me.util

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.fragment.app.DialogFragment
import org.kiimo.me.R

class ChoosePaymentMethodDialog(private val onItemChoosed: (item: Int) -> Unit) : DialogFragment() {

    companion object {
        fun newInstance(onItemChoosed: (item: Int) -> Unit): ChoosePaymentMethodDialog {
            return ChoosePaymentMethodDialog(onItemChoosed)
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val gender = ArrayList(arrayListOf("CASH", "CARD"))
        val arrayAdapter = ArrayAdapter<String>(activity!!, R.layout.dialog_chooose_item_payment)
        arrayAdapter.addAll(gender)
        val builder = AlertDialog.Builder(activity)
        val dialogInterface = DialogInterface.OnClickListener { dialogInterface, index ->
            onItemChoosed(index)
            dialogInterface.dismiss()
        }
        builder.setAdapter(arrayAdapter, dialogInterface)

        return builder.create()
    }
}