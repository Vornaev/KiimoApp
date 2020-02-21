package org.kiimo.me.util

import android.graphics.Typeface
import android.text.SpannableStringBuilder
import android.text.Spanned
import java.util.*

object SpanUtils {

    fun getSpannableString(
        startIndex: Int,
        string1: String,
        string2: String
    ): SpannableStringBuilder {
        val spannableString = SpannableStringBuilder(
            String.format(Locale.getDefault(), "%s %s", string1, string2)
        )

        spannableString.setSpan(
            CustomClickableSpannable(Typeface.DEFAULT_BOLD, null), startIndex,
            startIndex + 1 + string2.length, Spanned.SPAN_EXCLUSIVE_INCLUSIVE
        )

        return spannableString
    }
}