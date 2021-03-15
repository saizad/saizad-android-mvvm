package com.saizad.mvvmexample.components.main.home

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.saizad.mvvm.utils.throttleClick
import com.saizad.mvvmexample.R
import com.saizad.mvvmexample.components.main.MainFragment
import com.saizad.mvvmexample.components.main.users.ReqResUserItem
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_login.goToMain

@AndroidEntryPoint
class HomeFragment : MainFragment<HomeViewModel>() {

    override val viewModelClassType: Class<HomeViewModel>
        get() = HomeViewModel::class.java

    override fun onViewCreated(view: View, savedInstanceState: Bundle?, recycled: Boolean) {
        super.onViewCreated(view, savedInstanceState, recycled)
        goToMain.throttleClick {
            requireActivity().finish()
        }

        viewModel().delayed(2).observe(viewLifecycleOwner, Observer {
            showShortToast(it.size)
        })
        val reqResUserItem = currentUser as ReqResUserItem

        viewModel().user(1).observe(viewLifecycleOwner, Observer {
            reqResUserItem.bind(it)
        })

        users.throttleClick {
            findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToUsersFragment())
        }


    }

    override fun layoutRes(): Int {
        return R.layout.fragment_home
    }
}
