package com.saizad.mvvmexample.components.main.home

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.saizad.mvvm.enums.DataState
import com.saizad.mvvm.utils.stateToData
import com.saizad.mvvm.utils.throttleClick
import com.saizad.mvvmexample.ApiRequestCodes.DELAYED_RESPONSE
import com.saizad.mvvmexample.ApiRequestCodes.LONG_DELAYED_RESPONSE
import com.saizad.mvvmexample.ApiRequestCodes.SHORT_DELAYED_RESPONSE
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

        viewModel().resourceNotFound().observe(viewLifecycleOwner, Observer {
            if (it is DataState.Error) {
                showShortToast(it.throwable.message.toString())
            }
        })


        viewModel().delayed(1, DELAYED_RESPONSE)
            .observe(viewLifecycleOwner, Observer {
                when (it) {
                    is DataState.Success -> {
                        showShortToast(it.data.size)
                    }
                    is DataState.Loading -> {
                        if (it.isLoading) {
                            showShortToast("Loading....")
                        } else {
                            showShortToast("Loading Completed!!")
                        }
                    }
                }
            })

        viewModel().delayed(2, SHORT_DELAYED_RESPONSE)
            .stateToData()
            .observe(viewLifecycleOwner, Observer {
                showShortToast(it.size)
            })

        currentUserType.loggedInUser()
            .observe(viewLifecycleOwner, Observer {
                bindUserInfo(it)
            })

        users.throttleClick {
            viewModel().delayed(5, LONG_DELAYED_RESPONSE)
                .stateToData()
                .observe(viewLifecycleOwner, Observer {
                    showShortToast(it.size)
                })
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
