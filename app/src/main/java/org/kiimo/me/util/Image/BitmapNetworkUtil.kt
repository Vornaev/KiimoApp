package org.kiimo.me.util.Image

import android.graphics.Bitmap
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.ByteArrayOutputStream

object BitmapNetworkUtil {

    fun getMultipartBody(bitmap: Bitmap): MultipartBody.Part {

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



        stream.flush()
        stream.close()

        return body
    }
}