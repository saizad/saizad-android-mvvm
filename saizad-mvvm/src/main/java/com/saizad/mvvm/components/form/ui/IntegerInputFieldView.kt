package com.saizad.mvvm.components.form.ui

import android.content.Context
import android.util.AttributeSet
import com.sa.easyandroidform.ObjectUtils

open class IntegerInputFieldView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : InputFieldView<Int>(context, attrs, defStyleAttr) {

    override fun resolveFrom(charSequence: CharSequence): Int? {
        return try {
            Integer.valueOf(charSequence.toString())
        } catch (e: NumberFormatException) {
            null
        }
    }

    override fun resolve(value: Int?): CharSequence? {
        return if (ObjectUtils.isNull(value)) {
            null
        } else value.toString()
    }

    override fun isSame(value: Int, prevValue: Int): Boolean {
        return value == prevValue
    }
}