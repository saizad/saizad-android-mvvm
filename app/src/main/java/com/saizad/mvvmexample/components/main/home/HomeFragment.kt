package com.saizad.mvvmexample.components.main.home

import android.view.Menu
import android.view.MenuInflater
import android.view.View
import com.saizad.mvvm.ObjectPreference
import com.saizad.mvvm.ui.CountDrawable
import com.saizad.mvvmexample.R
import com.saizad.mvvmexample.components.main.MainFragment
import javax.inject.Inject

class HomeFragment : MainFragment<HomeViewModel>() {

    override fun getViewModelClassType(): Class<HomeViewModel> {
        return HomeViewModel::class.java
    }

    override fun layoutRes(): Int {
        return R.layout.fragment_home
    }

    override fun menRes(): Int {
        return R.menu.menu_home
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        val findItem = menu.findItem(R.id.counterMenu)
        CountDrawable.setCount(
            context(),
            findItem.icon,
            R.drawable.ic_home_black_24dp,
            (0..200).random()
        )
    }
}
