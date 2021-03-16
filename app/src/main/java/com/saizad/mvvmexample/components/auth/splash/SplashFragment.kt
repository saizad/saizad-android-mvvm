package com.saizad.mvvmexample.components.auth.splash

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import com.saizad.mvvm.utils.addToDisposable
import com.saizad.mvvm.utils.startActivityClear
import com.saizad.mvvmexample.R
import com.saizad.mvvmexample.components.auth.AuthFragment
import com.saizad.mvvmexample.components.main.MainActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SplashFragment : AuthFragment<SplashViewModel>() {

    override val viewModelClassType: Class<SplashViewModel>
        get() = SplashViewModel::class.java

    override fun onViewCreated(view: View, savedInstanceState: Bundle?, recycled: Boolean) {
        currentUserType.isLoggedIn
            .subscribe {
                if (!it) {
                    findNavController().navigate(SplashFragmentDirections.actionSplashFragmentToLoginFragment())
                } else {
                    context().startActivityClear<MainActivity>()
                }
            }.addToDisposable(compositeDisposable())
    }

    override fun layoutRes(): Int {
        return R.layout.fragment_splash
    }
}
