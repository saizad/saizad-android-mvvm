package com.saizad.mvvmexample.components.main.users.userpage

import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.widget.ViewPager2
import com.saizad.mvvm.pager.BaseFragmentStateAdapter

class UserPageAdapter(
    fragmentActivity: FragmentActivity,
    pages: List<Class<UserPageFragment>>,
    viewPager2: ViewPager2
) : BaseFragmentStateAdapter<UserPageFragment>(
        fragmentActivity,
        pages, viewPager2
    )