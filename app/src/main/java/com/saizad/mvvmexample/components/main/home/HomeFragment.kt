package com.saizad.mvvmexample.components.main.home

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.map
import androidx.navigation.fragment.findNavController
import com.saizad.mvvm.utils.throttleClick
import com.saizad.mvvmexample.R
import com.saizad.mvvmexample.components.main.MainFragment
import com.saizad.mvvmexample.components.main.users.ReqResUserItem
import com.saizad.mvvmexample.models.ReqResUser
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_home.*

@AndroidEntryPoint
class HomeFragment : MainFragment<HomeViewModel>() {

    private val reqResUserItem by lazy { currentUser as ReqResUserItem }

    override val viewModelClassType: Class<HomeViewModel>
        get() = HomeViewModel::class.java

    override fun onViewCreated(view: View, savedInstanceState: Bundle?, recycled: Boolean) {
        super.onViewCreated(view, savedInstanceState, recycled)

        logout.throttleClick {
            viewModel().logout()
        }

        viewModel().delayed(1).observe(viewLifecycleOwner, Observer {
            showShortToast(it.size)
        })

        viewModel().delayed(2).observe(viewLifecycleOwner, Observer {
            showShortToast(it.size)
        })

        currentUserType.loggedInUser()
            .observe(viewLifecycleOwner, Observer {
                bindUserInfo(it)
            })

        users.throttleClick {
            findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToUsersFragment())
        }

    }

    private fun bindUserInfo(reqResUser: ReqResUser) {
        reqResUserItem.bind(reqResUser)
        reqResUserItem.throttleClick {
            findNavController().navigate(
                HomeFragmentDirections.actionHomeFragmentToUpdateUserFragment(
                    reqResUser
                )
            )
        }
    }

    override fun layoutRes(): Int {
        return R.layout.fragment_home
    }
}
