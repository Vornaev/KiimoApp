package org.kiimo.me.util

import android.graphics.Paint
import android.graphics.Typeface
import android.text.TextPaint
import android.text.style.ClickableSpan
import android.view.View
import androidx.annotation.NonNull

class CustomClickableSpannable(
    private val type: Typeface?,
    private val onClickListener: OnClickListener?
) : ClickableSpan() {

    override fun updateDrawState(ds: TextPaint) {
        if (type != null) {
            applyCustomTypeFace(ds, type)
        }
    }

    private fun applyCustomTypeFace(paint: Paint, tf: Typeface) {
        val oldStyle: Int
        val old = paint.typeface
        oldStyle = old?.style ?: 0

        val fake = oldStyle and tf.style.inv()
        if (fake and Typeface.BOLD != 0) {
            paint.isFakeBoldText = true
        }

        if (fake and Typeface.ITALIC != 0) {
            paint.textSkewX = -0.25f
        }

        paint.typeface = tf
    }

    override fun onClick(@NonNull widget: View) {
        onClickListener?.onClick()
    }

    interface OnClickListener {
        fun onClick()
    }
}