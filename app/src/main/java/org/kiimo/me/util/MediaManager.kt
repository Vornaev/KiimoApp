package org.kiimo.me.util

import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import androidx.appcompat.app.AlertDialog
import androidx.core.content.FileProvider
import androidx.core.net.toUri
import androidx.exifinterface.media.ExifInterface
import org.kiimo.me.BuildConfig
import org.kiimo.me.R
import org.kiimo.me.app.App
import org.kiimo.me.app.BaseFragment
import java.io.File
import java.io.IOException
import java.util.*


object MediaManager {

    private var currentPhotoPath: String? = null
    var photoURI: Uri? = null
    const val REQUEST_IMAGE_CAPTURE = 1
    const val REQUEST_IMAGE_PICK = 2

    fun showMediaOptionsDialog(baseFragment: BaseFragment) {
        val dialogBuilder = AlertDialog.Builder(baseFragment.requireContext())

        val pickMediaOptions = arrayOf<CharSequence>(
            "take photo",
            "library"
        )

        val dialogListener = DialogInterface.OnClickListener { _, which ->
            when (which) {
                0 -> getDispatchTakePictureIntent(baseFragment)
                1 -> openMedia(baseFragment)
            }
        }

        dialogBuilder.setItems(pickMediaOptions, dialogListener)

        val dialog = dialogBuilder.create()

        val dialogNegativeButtonListener = DialogInterface.OnClickListener { dialogInterface, _ ->
            dialogInterface.cancel()
        }

        dialog.setButton(
            AlertDialog.BUTTON_NEGATIVE, baseFragment.getString(R.string.general_cancel_button),
            dialogNegativeButtonListener
        )

        dialog.show()
    }

    private fun openMedia(baseFragment: BaseFragment) {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        baseFragment.startActivityForResult(
            Intent.createChooser(
                intent,
                "select image"
            ), REQUEST_IMAGE_PICK
        )
    }

    private fun getDispatchTakePictureIntent(baseFragment: BaseFragment) {
        if (hasCamera(baseFragment.requireActivity())) {
            Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
                // Ensure that there's a camera activity to handle the intent
                takePictureIntent.resolveActivity(baseFragment.requireActivity().packageManager)
                    ?.also {
                        // Create the File where the photo should go
                        val photoFile: File? = try {
                            createImageFile(baseFragment.requireActivity())
                        } catch (ex: IOException) {
                            // Error occurred while creating the File
                            null
                        }
                        // Continue only if the File was successfully created
                        photoFile?.also {
                            val localPhotoURI = FileProvider.getUriForFile(
                                baseFragment.requireActivity(),
                                "${BuildConfig.APPLICATION_ID}.contentprovider",
                                it
                            )
                            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, localPhotoURI)

                            baseFragment.startActivityForResult(
                                takePictureIntent,
                                REQUEST_IMAGE_CAPTURE
                            )
                        }

                        photoURI = photoFile?.toUri()
                    }
            }
        }
    }

    private fun hasCamera(activity: Activity): Boolean =
        activity.packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY)

    @Throws(IOException::class)
    private fun createImageFile(context: Context): File {
        // Create an image file name
        val timeStamp: String = DateUtils.formatDateToTimestamp(Date())
        val storageDir: File? = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
            "JPEG_${timeStamp}_", /* prefix */
            ".jpg", /* suffix */
            storageDir /* directory */
        ).apply {
            // Save a file: path for use with ACTION_VIEW intents
            currentPhotoPath = absolutePath
        }
    }

    fun getImageFilename(): String {
        return this.currentPhotoPath ?: ""
    }

    private fun checkRotation(imageBitmap: Bitmap, currentPhotoPath: String): Bitmap {
        val ei = ExifInterface(currentPhotoPath)
        val orientation = ei.getAttributeInt(
            ExifInterface.TAG_ORIENTATION,
            ExifInterface.ORIENTATION_UNDEFINED
        )

        return when (orientation) {
            ExifInterface.ORIENTATION_ROTATE_90 -> rotateImage(imageBitmap, 90f)
            ExifInterface.ORIENTATION_ROTATE_180 -> rotateImage(imageBitmap, 180f)
            ExifInterface.ORIENTATION_ROTATE_270 -> rotateImage(imageBitmap, 270f)
            ExifInterface.ORIENTATION_NORMAL -> imageBitmap
            else -> imageBitmap
        }
    }

    private fun rotateImage(source: Bitmap, angle: Float): Bitmap {
        val matrix = Matrix()
        matrix.postRotate(angle)
        return Bitmap.createBitmap(
            source, 0, 0, source.width, source.height,
            matrix, true
        )
    }

    fun getBitmap(
        targetWidth: Float,
        targetHeight: Float
    ): Bitmap? {
        val bmOptions = BitmapFactory.Options()
        val currentPhotoPath = this.currentPhotoPath ?: return null
        BitmapFactory.decodeFile(currentPhotoPath, bmOptions)?.also { bitmap ->
            val bmOptionsScaled = BitmapFactory.Options().apply {
                // Get the dimensions of the bitmap
                inJustDecodeBounds = true

                val photoW: Float = bitmap.width.toFloat()
                val photoH: Float = bitmap.height.toFloat()

                // Determine how much to scale down the image
                val scaleFactor: Float =
                    (photoW / targetWidth).coerceAtMost(photoH / targetHeight)

                // Decode the image file into a Bitmap sized to fill the View
                inJustDecodeBounds = false
                inSampleSize = scaleFactor.toInt()
            }
            BitmapFactory.decodeFile(currentPhotoPath, bmOptionsScaled)?.also { scaledBitmap ->
                return checkRotation(scaledBitmap, currentPhotoPath)
            }
        }

        return null
    }

    fun getBitmapGallery(
        bitmap: Bitmap,
        targetWidth: Int,
        targetHeight: Int
    ): Bitmap? {
        return Bitmap.createScaledBitmap(bitmap, targetWidth, targetHeight, false)
    }
}