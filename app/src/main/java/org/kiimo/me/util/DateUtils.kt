package org.kiimo.me.util

import java.text.SimpleDateFormat
import java.util.*

object DateUtils {

    private val imageTimestamp = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.US)

    fun formatDateToTimestamp(date: Date): String {
        return imageTimestamp.format(date)
    }
}