package com.saizad.mvvmexample.components.main.home

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.saizad.mvvm.enums.DataState
import com.saizad.mvvm.utils.lifecycleScopeOnMain
import com.saizad.mvvm.utils.noContentStateToData
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
import kotlinx.coroutines.flow.collect

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

        lifecycleScopeOnMain {
            viewModel().delete()
                .noContentStateToData()
                .collect {
                    showShortToast("Deleted")
                }
        }


        lifecycleScopeOnMain {
            viewModel().resourceNotFound()
                .collect {
                    if (it is DataState.Error) {
                        showShortToast(it.throwable.message.toString())
                    }
                }
        }

        lifecycleScopeOnMain {
            viewModel().delayed(1, DELAYED_RESPONSE)
                .collect {
                    when (it) {
                        is DataState.Success -> {
                            showShortToast(it.data!!.data.size)
                        }
                        is DataState.Loading -> {
                            if (it.isLoading) {
                                showShortToast("Loading....")
                            } else {
                                showShortToast("Loading Completed!!")
                            }
                        }
                    }
                }
        }

        lifecycleScopeOnMain {
            viewModel().delayed(2, SHORT_DELAYED_RESPONSE)
                .stateToData()
                .collect {
                    showShortToast(it.data.size)
                }
        }


        currentUserType.loggedInUser()
            .observe(viewLifecycleOwner, Observer {
                bindUserInfo(it)
            })

        users.throttleClick {
            lifecycleScopeOnMain {
                viewModel().delayed(2, LONG_DELAYED_RESPONSE)
                    .stateToData()
                    .collect {
                        showShortToast(it.data.size)
                    }
            }

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

