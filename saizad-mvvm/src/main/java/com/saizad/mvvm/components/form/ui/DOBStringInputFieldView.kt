package com.saizad.mvvm.components.form.ui

import android.content.Context
import android.util.AttributeSet
import com.saizad.mvvm.utils.Utils
import org.joda.time.DateTime

open class DOBStringInputFieldView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : InputFieldView<String>(context, attrs, defStyleAttr) {

    override fun resolveFrom(charSequence: CharSequence): String? {
        return if (charSequence.toString().isEmpty()) {
            null
        } else DateTime.parse(charSequence.toString())
            .toString(Utils.APP_DATE_FORMATTER)
    }

    override fun resolve(value: String?): CharSequence? {
        return if (value == null) {
            null
        } else DateTime.parse(value)
            .toString(Utils.APP_DATE_FORMATTER)
    }

    override fun isSame(value: String, prevValue: String): Boolean {
        return value == prevValue
    }
}