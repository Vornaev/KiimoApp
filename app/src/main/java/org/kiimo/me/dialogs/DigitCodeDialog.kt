package org.kiimo.me.dialogs

import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import org.kiimo.me.R
import org.kiimo.me.databinding.DialogDigitCodeBinding
import java.util.*

class DigitCodeDialog : BaseKiimoDialog() {

    private lateinit var binding: DialogDigitCodeBinding

    interface OnDigitCodeDialogListener {
        fun onDigitCodeDialogEnd()
        fun onValidateCode(code: String)
    }

    @SuppressLint("InflateParams")
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = DataBindingUtil.inflate(
            LayoutInflater.from(activity),
            R.layout.dialog_digit_code,
            null,
            false
        )

        val builder = AlertDialog.Builder(activity!!)
        builder.setPositiveButton(getString(R.string.dialog_confirm), null)

        builder.setView(binding.root)

        return builder.create()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dialog?.setOnShowListener {
            val button = (dialog as AlertDialog).getButton(AlertDialog.BUTTON_POSITIVE)
            button.setOnClickListener {
                onPositiveButtonClicked()
            }
        }

        return super.onCreateView(inflater, container, savedInstanceState)
    }

    private fun onPositiveButtonClicked() {
        val textString = binding.enterFiveDigitCodeEditText.text.toString()
        val length = textString.length
        if (length == 5) {
            (activity as OnDigitCodeDialogListener).onValidateCode(textString)
        } else {
            Toast.makeText(
                context,
                String.format(
                    Locale.getDefault(), "%s %d %s",
                    getString(R.string.you_have_entered), length, getString(R.string.digits)
                ),
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    fun dismissDialog() {
        (activity as OnDigitCodeDialogListener).onDigitCodeDialogEnd()
        dismiss()
    }

    fun showDigitCodeDialogValidationError() {
        binding.invalidCodeTextView.visibility = View.VISIBLE
    }
}