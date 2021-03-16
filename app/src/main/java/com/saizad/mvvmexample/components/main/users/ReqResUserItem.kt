package com.saizad.mvvmexample.components.main.users

import android.content.Context
import android.util.AttributeSet
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.saizad.mvvm.FullWidthListItem
import kotlinx.android.synthetic.main.item_req_res_user.view.*
import com.saizad.mvvmexample.R
import com.saizad.mvvmexample.models.ReqResUser;

class ReqResUserItem @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FullWidthListItem<ReqResUser>(context, attrs, defStyleAttr) {

    override fun bind(i: ReqResUser) {
        super.bind(i)
        Glide.with(context)
            .load(i.avatar)
            .transform(CircleCrop(), CenterCrop())
            .into(avatar)

        fullNameField.text = i.fullName
        email.text = i.email
        job.text = i.job
        job.isVisible = i.job != null
    }

    override fun itemGapSize(): Int {
        return resources.getDimensionPixelSize(R.dimen.space_1x)
    }
}