package com.saizad.mvvmexample.components.auth.login

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import com.saizad.mvvm.utils.throttleClick
import com.saizad.mvvmexample.R
import com.saizad.mvvmexample.components.auth.AuthFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_login.*

@AndroidEntryPoint
class LoginFragment : AuthFragment<LoginViewModel>() {

    override val viewModelClassType: Class<LoginViewModel>
        get() = LoginViewModel::class.java

    override fun onViewCreated(view: View, savedInstanceState: Bundle?, recycled: Boolean) {

        viewModel().toastLiveData.observe(lifecycleOwner, Observer {
            showLongToast(it)
        })

        goToMain.throttleClick{
//            startActivity(Intent(context(), DrawerMainActivity::class.java))
        }
    }

    override fun layoutRes(): Int {
        return R.layout.fragment_login
    }
}
