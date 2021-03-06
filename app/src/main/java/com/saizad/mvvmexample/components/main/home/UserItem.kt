package com.saizad.mvvmexample.components.main.home

import android.content.Context
import android.util.AttributeSet
import kotlinx.android.synthetic.main.item_user.view.*
import com.saizad.mvvm.SaizadListItem
import com.saizad.mvvmexample.models.User;

class UserItem @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : SaizadListItem<User>(context, attrs, defStyleAttr) {

    override fun bind(i: User) {
        super.bind(i)
    }
}