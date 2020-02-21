package org.kiimo.me.custom.views

import android.content.Context
import android.util.AttributeSet
import android.widget.NumberPicker
import org.kiimo.me.R

class CustomNumberPicker : NumberPicker {

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        processAttributeSet(attrs)
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(
        context,
        attrs,
        defStyle
    ) {
        processAttributeSet(attrs)
    }

    private fun processAttributeSet(
        attrs: AttributeSet,
        defStyleAttr: Int = 0,
        defStyleRes: Int = 0
    ) {
        val attributes = context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.CustomNumberPicker,
            defStyleAttr,
            defStyleRes
        )

        try {
            this.minValue = attributes.getInt(R.styleable.CustomNumberPicker_minValue, 0)
            this.maxValue = attributes.getInt(R.styleable.CustomNumberPicker_maxValue, 0)
            this.value = attributes.getInt(R.styleable.CustomNumberPicker_defaultValue, 0)
        } finally {
            attributes.recycle()
        }
    }
}