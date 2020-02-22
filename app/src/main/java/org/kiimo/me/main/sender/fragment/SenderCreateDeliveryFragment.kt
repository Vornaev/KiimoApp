package org.kiimo.me.main.sender.fragment

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import kotlinx.android.synthetic.main.fragment_sender_create_item_details.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.kiimo.me.app.BaseMainFragment
import org.kiimo.me.databinding.FragmentSenderCreateItemDetailsBinding
import org.kiimo.me.main.menu.mainViewModel.MainMenuViewModel
import org.kiimo.me.main.sender.SenderKiimoActivity
import org.kiimo.me.models.Type
import org.kiimo.me.util.MediaManager
import org.kiimo.me.util.PACKAGE_SIZE_ID
import java.io.ByteArrayOutputStream
import java.io.File


class SenderCreateDeliveryFragment : BaseMainFragment() {

    lateinit var binding: FragmentSenderCreateItemDetailsBinding
    val viewModel: MainMenuViewModel by lazy { getNavigationActivity().viewModel }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)



        viewModel.photoLiveData.observe(viewLifecycleOwner, Observer {
            val p = it
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentSenderCreateItemDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.havePhoto = false

        atachPhotoListeners()
        onRadioButtonChanged()
        onDeliveryPackageReady()

    }

    private fun onDeliveryPackageReady() {
        binding.sendItemCreateDeliveryButton.setOnClickListener {
            viewModel.senderProperties.packageDescritpion.description =
                binding.sendItemDescriptionEditField.text.toString()
            (activity as SenderKiimoActivity).openPackageDetailsFragment()
        }

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
                radioButtonSmallDelivery.id -> viewModel.setPackageSize(PACKAGE_SIZE_ID.SMALL)
                radioButtonMediumDelivery.id -> viewModel.setPackageSize(PACKAGE_SIZE_ID.MEDIUM)
                radioButtonLargeDelivery.id -> viewModel.setPackageSize(PACKAGE_SIZE_ID.LARGE)
            }
        }
    }

    private fun onSuccessGetImage(bitmap: Bitmap) {
        binding.havePhoto = true
        binding.imageViewDeliveryPackage.setImageBitmap(bitmap)
        uploadBitmap(bitmap)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {

            var photoUri: Uri

            val width = 300
            //requireContext().resources.getDimensionPixelSize(R.dimen.createDeliveryImageWidth)
            val height = 250
            //requireContext().resources.getDimensionPixelSize(R.dimen.createDeliveryImageHeight)
            if (width != null && height != null && width > 0 && height > 0) {
                if (requestCode == MediaManager.REQUEST_IMAGE_CAPTURE) {

                    photoUri = Uri.parse(MediaManager.getImageFilename())
                    // uploadImage(photoUri)

                    MediaManager.getBitmap(
                        width.toFloat(), height.toFloat()
                    )?.apply {
                        onSuccessGetImage(this)
                    }
                } else if (requestCode == MediaManager.REQUEST_IMAGE_PICK) {
                    val uri = data?.data
                    uploadImage(uri)

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
        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, stream)
        val byteArray = stream.toByteArray()

        val fileRequest = RequestBody.create(
            MediaType.parse("image/*"),
            byteArray
        )

        val body = MultipartBody.Part.createFormData(
            "media",
            "${System.currentTimeMillis()}.jpg",
            fileRequest
        );

        viewModel.uploadPhotoToKiimo(body)

        // bitmap.recycle()
    }

    fun uploadImage(uri: Uri?) {

        val file = File(MediaManager.getImageFilename())

        if (file.exists()) {

            val fileRequest = RequestBody.create(
                MediaType.parse("image/*"),
                file
            )

            val body = MultipartBody.Part.createFormData("media", file.getName(), fileRequest);

            viewModel.uploadPhotoToKiimo(body)

        }

        uri?.let {
            //  viewModel.uploadPhotoToServer(UploadPhotoRequest(MediaManager.fileOrigis!!.readBytes(), Type.Packages))
        }
    }
}