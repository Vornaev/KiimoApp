package org.kiimo.me.models

import android.graphics.Bitmap
import java.io.File
import java.io.FileInputStream

data class UploadPhotoRequest(

    var media: FileInputStream? = null,
    var type: String = ""
)

object Type {

    const val Users = "users"
    const val Packages = "packages"
    const val Signatures = "signatures"
    const val PersionalID = "personalID"


}