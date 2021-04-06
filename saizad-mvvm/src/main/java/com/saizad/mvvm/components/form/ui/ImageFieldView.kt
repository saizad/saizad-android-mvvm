package com.saizad.mvvm.components.form.ui

import android.content.Context
import android.content.Intent
import android.provider.MediaStore
import android.util.AttributeSet
import android.view.View
import com.saizad.mvvm.R
import com.saizad.mvvm.utils.ImageUtils
import com.saizad.mvvm.utils.ViewUtils
import kotlinx.android.synthetic.main.lib_image_field_view.view.*


open class ImageFieldView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
    FilesFieldView(context, attrs, defStyleAttr) {

    override fun showValue(field: List<String>?) {
        super.showValue(field)
        ImageUtils.setAvatarImage(image_view, field?.first())
    }

    override fun initView(): View {
        return edit_avatar
    }

    override fun undoView(): View? {
        return undo_avatar
    }

    override fun clearView(): View? {
        return deleteAvatar
    }
    
    init {
        ViewUtils.inflate(getContext(), R.layout.lib_image_field_view, this, true)
    }

    override fun intent(): Intent {
        val intent = Intent(
            Intent.ACTION_PICK,
            MediaStore.Images.Media.INTERNAL_CONTENT_URI
        )
        intent.type = "image/*"
        intent.putExtra("crop", "true")
        intent.putExtra("scale", true)
        intent.putExtra("outputX", 256)
        intent.putExtra("outputY", 256)
        intent.putExtra("aspectX", 1)
        intent.putExtra("aspectY", 1)
        intent.putExtra("return-customers", true)
        return intent
    }
}
