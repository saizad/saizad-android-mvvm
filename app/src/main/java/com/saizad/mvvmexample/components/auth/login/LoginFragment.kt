package com.saizad.mvvmexample.components.auth.login

import android.os.Bundle
import android.text.method.PasswordTransformationMethod
import android.view.View
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.saizad.mvvm.utils.lifecycleScopeOnMainWithDelay
import com.saizad.mvvm.utils.startActivityClear
import com.saizad.mvvm.utils.stateToData
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

        logout.throttleClick {
            viewModel().login()
                .observe(viewLifecycleOwner, Observer {
                    viewModel().user((1..10).random())
                        .stateToData()
                        .observe(viewLifecycleOwner, Observer {
                            currentUserType.refresh(it)
                            context().startActivityClear<MainActivity>()
                        })
                })
        }

        lifecycleScopeOnMainWithDelay(1000) {
            passwordField.getEditText().transformationMethod =
                PasswordTransformationMethod.getInstance()
        }

        viewModel().loginFormLiveData.observe(viewLifecycleOwner, Observer { emailLoginForm ->
            emailField.setField(emailLoginForm.emailField)
            passwordField.setField(emailLoginForm.passwordField)
            emailLoginForm.validObservable()
                .subscribe {
                    logout.isEnabled = it
                }
        })

        view.throttleClick {
            findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToBlankFragment())
        }
    }

    override fun layoutRes(): Int {
        return R.layout.fragment_login
    }

    override fun persistView(): Boolean {
        return false
    }
}
