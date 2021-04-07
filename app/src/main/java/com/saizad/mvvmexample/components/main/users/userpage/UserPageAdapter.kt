package com.saizad.mvvmexample.components.main.users.userpage

import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.saizad.mvvm.pager.BaseFragmentStateAdapter

class UserPageAdapter(
    fragment: Fragment,
    pages: List<Class<UserPageFragment>>,
    viewPager2: ViewPager2
) : BaseFragmentStateAdapter<UserPageFragment>(
        fragment,
        pages, viewPager2
    )