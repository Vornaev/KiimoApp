package org.kiimo.me.main.sender.fragment

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.fragment_sender_create_item_details.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.kiimo.me.R
import org.kiimo.me.app.BaseMainFragment
import org.kiimo.me.databinding.FragmentSenderCreateItemDetailsBinding
import org.kiimo.me.main.menu.mainViewModel.MainMenuViewModel
import org.kiimo.me.main.sender.SenderKiimoActivity
import org.kiimo.me.util.MediaManager
import org.kiimo.me.util.PACKAGE_SIZE_ID
import org.w3c.dom.Text
import java.io.ByteArrayOutputStream
import java.io.File


class SenderCreateDeliveryFragment : BaseMainFragment() {

    lateinit var binding: FragmentSenderCreateItemDetailsBinding
    val viewModel: MainMenuViewModel by lazy { getNavigationActivity().viewModel }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel.senderProperties.packageDescritpion.images.clear()

        viewModel.photoPackageLiveData.observe(viewLifecycleOwner, Observer {
            viewModel.senderProperties.packageDescritpion.images.clear()
            viewModel.senderProperties.packageDescritpion.images.add(it.imageUrl)
            hideSpinner()
        })
    }




    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentSenderCreateItemDetailsBinding.inflate(inflater, container, false)
        binding.havePhoneNumber = true
        binding.hasDescription = true
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        attachListenerToField(binding.sendItemDescriptionEditField, ::validateDescription)
        attachListenerToField(binding.sendItemDescriptionPhoneField, ::validatePhoneNumber)
        binding.havePhoto = false

        atachPhotoListeners()
        onRadioButtonChanged()
        onDeliveryPackageReady()

    }

    private fun onDeliveryPackageReady() {
        binding.sendItemCreateDeliveryButton.setOnClickListener {

            if (validateFields()) {
                viewModel.senderProperties.packageDescritpion.description = binding.sendItemDescriptionEditField.text.toString()
                viewModel.senderProperties.packageDescritpion.phoneNumber = binding.sendItemDescriptionPhoneField.text.toString()
                (activity as SenderKiimoActivity).openPackageDetailsFragment()

            } else {
                return@setOnClickListener
            }
        }
    }

    private fun validateFields(): Boolean {
        var i = 0

        if (!validateDescription()) ++i
        if (!validatePhoneNumber()) ++i
        return i == 0
    }

    private fun validateDescription(): Boolean {
        binding.hasDescription = binding.sendItemDescriptionEditField.text.length >= 3
        return binding.hasDescription
    }

    private fun validatePhoneNumber(): Boolean {
        val patternText = binding.sendItemDescriptionPhoneField.text.toString()
        binding.havePhoneNumber =patternText.length >= 8 && TextUtils.isDigitsOnly(patternText.substring(1))
        return binding.havePhoneNumber
    }

    private fun atachPhotoListeners() {
        binding.sendItemCreateUploadPhoto.setOnClickListener {
            MediaManager.showMediaOptionsDialog(this)
        }

        binding.closeUploadPhotoView.setOnClickListener {
            binding.havePhoto = false
        }
    }

    private fun onRadioButtonChanged() {
        binding.radioGroupCreateDeliverySize.setOnCheckedChangeListener { radioGroup, i ->
            when (i) {
                radioButtonSmallDelivery.id -> {
                    viewModel.setPackageSize(PACKAGE_SIZE_ID.SMALL)
                }
                radioButtonMediumDelivery.id -> viewModel.setPackageSize(PACKAGE_SIZE_ID.MEDIUM)
                radioButtonLargeDelivery.id -> viewModel.setPackageSize(PACKAGE_SIZE_ID.LARGE)
            }
        }
    }

    private fun onSuccessGetImage(bitmap: Bitmap) {
        showSpinner()
        binding.havePhoto = true
        Glide.with(requireContext()).load(bitmap).override(300, 0).centerCrop()
            .into(binding.imageViewDeliveryPackage)
        //binding.imageViewDeliveryPackage.setImageBitmap(bitmap)
        uploadBitmap(bitmap)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {

            val width = 480
            val height = 640

            if (width != null && height != null && width > 0 && height > 0) {
                if (requestCode == MediaManager.REQUEST_IMAGE_CAPTURE) {

                    MediaManager.getBitmap(
                        width.toFloat(), height.toFloat()
                    )?.apply {
                        onSuccessGetImage(this)
                    }
                } else if (requestCode == MediaManager.REQUEST_IMAGE_PICK) {
                    val uri = data?.data

                    MediaManager.getBitmapGallery(
                        MediaStore.Images.Media.getBitmap(context?.contentResolver, uri),
                        width, height
                    )?.apply {
                        onSuccessGetImage(this)
                    }
                }
            }
        }
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

        viewModel.uploadPhotoForPackage(body)
    }

    fun uploadImage(uri: Uri?) {

        val file = File(MediaManager.getImageFilename())

        if (file.exists()) {

            val fileRequest = RequestBody.create(
                MediaType.parse("image/*"),
                file
            )

            val body = MultipartBody.Part.createFormData("media", file.getName(), fileRequest);

            viewModel.uploadPhotoForPackage(body)
        }

        uri?.let {
            //  viewModel.uploadPhotoToServer(UploadPhotoRequest(MediaManager.fileOrigis!!.readBytes(), Type.Packages))
        }
    }

    private fun showSpinner(){
        camomileSpinner.visibility = View.VISIBLE
        camomileSpinner.start()
    }

    private fun hideSpinner(){
        camomileSpinner.visibility = View.INVISIBLE
        camomileSpinner.stop()
    }
}