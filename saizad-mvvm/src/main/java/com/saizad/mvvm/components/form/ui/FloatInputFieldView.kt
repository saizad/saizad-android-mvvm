package com.saizad.mvvm.components.form.ui

import android.content.Context
import android.util.AttributeSet
import com.sa.easyandroidform.ObjectUtils

class FloatInputFieldView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : InputFieldView<Float>(context, attrs, defStyleAttr) {


    override fun resolveFrom(charSequence: CharSequence): Float? {
        return try {
            java.lang.Float.valueOf(charSequence.toString())
        } catch (e: NumberFormatException) {
            null
        }
    }

    override fun resolve(value: Float?): CharSequence? {
        return if (ObjectUtils.isNull(value)) {
            null
        } else value.toString()
    }

    override fun isSame(value: Float, prevValue: Float): Boolean {
        return value == prevValue
    }
}