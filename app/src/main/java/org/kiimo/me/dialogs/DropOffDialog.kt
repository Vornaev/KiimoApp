package org.kiimo.me.dialogs

import android.app.Dialog
import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import kotlinx.android.synthetic.main.dialog_signature_drop_package.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.kiimo.me.R
import org.kiimo.me.databinding.DialogSignatureDropPackageBinding
import org.kiimo.me.main.menu.KiimoMainNavigationActivity
import org.kiimo.me.models.DeliveryPrice
import org.kiimo.me.util.DialogUtils
import java.io.ByteArrayOutputStream

class DropOffDialog : DialogFragment() {


    lateinit var binding: DialogSignatureDropPackageBinding

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
        builder.setView(binding.root).setCancelable(false)

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

                if (binding.signaturePad.isEmpty) {
                    DialogUtils.ShowInfoDialog(
                        requireActivity(),
                        "You must enter signature to continue"
                    )
                } else {
                    val bitmap = binding.signaturePad.signatureBitmap
                    uploadBitmap(bitmap)
                }

            }
        }

        return super.onCreateView(inflater, container, savedInstanceState)
    }

    fun dismissDialog(deliveryPrice: DeliveryPrice) {
        (activity as OnDropOffDialogListener).onDropOffDialogEnd(deliveryPrice)
        dismiss()
    }

    fun uploadBitmap(bitmap: Bitmap) {

        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
        var byteArray = stream.toByteArray()

        val fileRequest = RequestBody.create(
            MediaType.parse("image/*"),
            byteArray
        )

        val body = MultipartBody.Part.createFormData(
            "media",
            "${System.currentTimeMillis()}.jpg",
            fileRequest
        );

        (requireActivity() as KiimoMainNavigationActivity).viewModel.uploadSignaturePhoto(body)
    }

//    fun onSuccessUploadedPhoto() {
//        (requireActivity() as KiimoMainNavigationActivity).viewModel.signatureLiveData.observe(
//            viewLifecycleOwner, Observer {
//                val signatureDefault =
//                    "https://img.deliverycoin.net/signatures/2019/11/5508abb2-0756-4827-ace7-95131b2b4986.png"
//
//                val res = it.imageUrl
//                //(activity as OnDropOffDialogListener).onDropOff(it.imageUrl)
//            }
//        )
//    }
}