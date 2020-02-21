package org.kiimo.me.main.sender.dialog

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import org.kiimo.me.R
import org.kiimo.me.databinding.DialogSendItemDescriptionBinding
import org.kiimo.me.dialogs.BaseKiimoDialog
import android.view.animation.LinearInterpolator
import androidx.core.view.ViewCompat.animate



class SendItemDescriptionDialog : BaseKiimoDialog() {

    lateinit var binding: DialogSendItemDescriptionBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {


        binding = DataBindingUtil.inflate(
            LayoutInflater.from(context),
            R.layout.dialog_send_item_description,
            null,
            false
        )

        val builder = AlertDialog.Builder(context!!)

        binding.spinnerImageView.animate().rotationBy(42600f)
            .setDuration(600000).setInterpolator(LinearInterpolator()).start()


        builder.setView(binding.root)

        return builder.create()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return super.onCreateView(inflater, container, savedInstanceState)
    }

    fun animateImage(){

    }
}