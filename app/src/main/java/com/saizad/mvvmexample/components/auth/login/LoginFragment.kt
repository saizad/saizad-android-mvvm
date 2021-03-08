package com.saizad.mvvmexample.components.auth.login

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import com.saizad.mvvm.utils.startActivity
import com.saizad.mvvm.utils.throttleClick
import com.saizad.mvvmexample.R
import com.saizad.mvvmexample.components.auth.AuthFragment
import com.saizad.mvvmexample.components.main.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_login.*

@AndroidEntryPoint
class LoginFragment : AuthFragment<LoginViewModel>() {

    override val viewModelClassType: Class<LoginViewModel>
        get() = LoginViewModel::class.java

    override fun onViewCreated(view: View, savedInstanceState: Bundle?, recycled: Boolean) {

        goToMain.throttleClick {
            viewModel().login().observe(viewLifecycleOwner, Observer {
                context().startActivity<MainActivity>()
            })
        }

        viewModel().loginFormLiveData.observe(viewLifecycleOwner, Observer {
            emailField.setField(it.emailField)
            passwordField.setField(it.passwordField)
            it.validObservable()
                .subscribe {
                    goToMain.isEnabled = it
                }
        })
    }

    override fun layoutRes(): Int {
        return R.layout.fragment_login
    }
}
