package com.saizad.mvvmexample.components.main.home

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import com.saizad.mvvm.ActivityResult
import com.saizad.mvvm.SaizadLocation
import com.saizad.mvvm.model.UserInfo
import com.saizad.mvvm.utils.throttleClick
import com.saizad.mvvmexample.R
import com.saizad.mvvmexample.components.auth.AuthFragment
import com.saizad.mvvmexample.components.main.MainFragment
import com.saizad.mvvmexample.di.auth.AuthEnvironment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_login.*
import kotlinx.android.synthetic.main.fragment_login.goToMain
import javax.inject.Inject
import javax.inject.Named

@AndroidEntryPoint
class HomeFragment : MainFragment<HomeViewModel>() {

    override val viewModelClassType: Class<HomeViewModel>
        get() = HomeViewModel::class.java

    override fun onViewCreated(view: View, savedInstanceState: Bundle?, recycled: Boolean) {
        super.onViewCreated(view, savedInstanceState, recycled)
        goToMain.throttleClick {
            requireActivity().finish()
        }
    }

    override fun layoutRes(): Int {
        return R.layout.fragment_home
    }
}
