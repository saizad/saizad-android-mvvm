package com.saizad.mvvm.components.form.ui

import android.content.Context
import android.util.AttributeSet

class StringInputFieldView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : InputFieldView<String>(context, attrs, defStyleAttr) {

    override fun resolveFrom(charSequence: CharSequence): String? {
        return if (charSequence.toString().isEmpty()) {
            null
        } else charSequence.toString()
    }

    override fun resolve(value: String?): CharSequence? {
        return value
    }

    override fun isSame(value: String, prevValue: String): Boolean {
        return value == prevValue
    }
}