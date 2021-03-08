package com.saizad.mvvmexample.components.auth.splash

import android.os.Bundle
import android.view.View
import com.saizad.mvvmexample.R
import com.saizad.mvvmexample.components.auth.AuthFragment
import io.reactivex.Observable
import java.util.concurrent.TimeUnit
import androidx.navigation.fragment.findNavController
import com.saizad.mvvm.model.UserInfo
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SplashFragment : AuthFragment<SplashViewModel>() {

    override val viewModelClassType: Class<SplashViewModel>
        get() = SplashViewModel::class.java

    override fun onViewCreated(view: View, savedInstanceState: Bundle?, recycled: Boolean) {
        Observable.just("")
            .subscribeOn(schedulerProviderIO)
            .delay(100, TimeUnit.MILLISECONDS)
            .observeOn(schedulerProviderUI)
            .subscribe {
                findNavController().navigate(SplashFragmentDirections.actionSplashFragmentToLoginFragment(
                    UserInfo().apply {
                        this.userName = "hello"
                        this.email = "asdfad@gmail.com"
                        this.mobile = "9999999999"
                    }
                ))
            }
    }

    override fun layoutRes(): Int {
        return R.layout.fragment_splash
    }
}
