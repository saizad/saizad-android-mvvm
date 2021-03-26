package com.saizad.mvvmexample.components.main.users.userpage

import androidx.fragment.app.FragmentActivity
import com.saizad.mvvm.pager.BaseFragmentStateAdapter

class UserPageAdapter(fragmentActivity: FragmentActivity, pages: List<Class<UserPageFragment>>) :
    BaseFragmentStateAdapter<UserPageFragment>(
        fragmentActivity,
        pages
    )