package com.saizad.mvvmexample.components.auth.login

import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.View
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.saizad.mvvm.utils.lifecycleScopeOnMainWithDelay
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

        lifecycleScopeOnMainWithDelay(1000) {
            passwordField.getEditText().transformationMethod =
                PasswordTransformationMethod.getInstance()
        }

        viewModel().loginFormLiveData.observe(viewLifecycleOwner, Observer { emailLoginForm ->
            emailField.setField(emailLoginForm.emailField)
            passwordField.setField(emailLoginForm.passwordField)
            emailLoginForm.emailField.observable()
                .subscribe {
                    log("Email value -> ${emailLoginForm.emailField.field}")
                }
            emailLoginForm.validObservable()
                .subscribe {
                    log("Email value form(isValid = $it) -> ${emailLoginForm.emailField.field}")
                    goToMain.isEnabled = it
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
