package com.saizad.mvvm.components.form.fields

import com.sa.easyandroidform.StringUtils
import com.sa.easyandroidform.fields.NonEmptyStringField
import com.saizad.mvvm.components.form.validateName
import io.reactivex.exceptions.CompositeException
import kotlin.jvm.Throws

class NameField(
    fieldId: String,
    val name: String,
    isMandatory: Boolean
) : NonEmptyStringField(fieldId, name, isMandatory) {

    fun firstName(): String? = getName(0)

    fun lastName(): String? = getName(1)

    private fun getName(index: Int): String? {
        if (isValid) {
            val split =
                StringUtils.stripTrailingLeadingNewLines(field)
                    .split(" ".toRegex()).toTypedArray()
            return split[index]
        }
        return null
    }

    @Throws(CompositeException::class)
    override fun validate() {
        super.validate()
        validateName(field)
    }
}