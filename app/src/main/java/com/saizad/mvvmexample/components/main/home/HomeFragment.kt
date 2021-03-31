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
import com.saizad.mvvmexample.ApiRequestCodes.DEFAULT_ERROR
import com.saizad.mvvmexample.ApiRequestCodes.DELAYED_RESPONSE
import com.saizad.mvvmexample.ApiRequestCodes.RANDOM_REQUEST
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

        noContentRequest.throttleClick {
            lifecycleScopeOnMain {
                viewModel().noContentResponse()
                    .noContentStateToData()
                    .collect {
                        showShortToast("No Content")
                    }
            }
        }

        resNotFoundErrorResponse.throttleClick {
            lifecycleScopeOnMain {
                viewModel().resourceNotFound()
                    .collect {
                        if (it is DataState.ApiError) {
                            showShortToast(it.apiErrorException.errorModel.message())
                        }
                    }
            }
        }

        multiRequests.throttleClick {
            lifecycleScopeOnMain {
                viewModel().delayed(5, DELAYED_RESPONSE)
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
                viewModel().delayed(6, SHORT_DELAYED_RESPONSE)
                    .stateToData()
                    .collect {
                        showShortToast(it.data.size)
                    }
            }
        }

        requestNoLoading.throttleClick {
            lifecycleScopeOnMain {
                viewModel().delayed(1, RANDOM_REQUEST)
                    .stateToData()
                    .collect {
                        showShortToast(it.data.size)
                    }
            }
        }

        defaultError.throttleClick {
            lifecycleScopeOnMain {
                viewModel().resourceNotFound(DEFAULT_ERROR)
                    .collect {}
            }
        }


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

