package org.kiimo.me.models

import android.graphics.Bitmap
import java.io.File

data class UploadPhotoRequest(

    var media: File? = null,
    var type: String = ""
)

object Type {

    const val Users = "users"
    const val Packages = "packages"
    const val Signatures = "signatures"
    const val PersionalID = "personalID"


}