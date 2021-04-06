package com.saizad.mvvm.components.form.ui

import android.content.Context
import android.util.AttributeSet
import com.sa.easyandroidform.ObjectUtils

open class LongInputFieldView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : InputFieldView<Long>(context, attrs, defStyleAttr) {


    override fun resolveFrom(charSequence: CharSequence): Long? {
        return try {
            java.lang.Long.valueOf(charSequence.toString())
        } catch (e: NumberFormatException) {
            null
        }
    }

    override fun resolve(value: Long?): CharSequence? {
        return if (ObjectUtils.isNull(value)) {
            null
        } else value.toString()
    }

    override fun isSame(value: Long, prevValue: Long): Boolean {
        return value == prevValue
    }
}