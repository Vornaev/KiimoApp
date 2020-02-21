package org.kiimo.me.util.Image

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.RectF
import android.media.MediaScannerConnection
import android.os.Environment
import android.util.Base64
import android.util.Log
import android.view.View
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.*

object ImageUtil {

    private val PROFILE_IMAGE_DIRECTORY = "/userProfile"


    fun getBitmapFromView(view: View, width: Int, height: Int): Bitmap {
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        view.draw(canvas)
        return bitmap
    }
}