package org.kiimo.me.util

import java.text.SimpleDateFormat
import java.util.*

object DateUtils {

    val DATE_FORMAT_API = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.US)
    val DATE_FORMAT_FULL = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS", Locale.US)
    val DISPLAY_FORMAT = SimpleDateFormat("dd-MM-yyyy", Locale.US)


    private val imageTimestamp = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.US)

    fun formatDateToTimestamp(date: Date): String {
        return imageTimestamp.format(date)
    }

    fun formatDate(inputDate: String): String {

        try {
            val res = DISPLAY_FORMAT.format(DATE_FORMAT_FULL.parse(inputDate))
            return res
        } catch (ex: Exception) {
            return ""
        }
    }
}