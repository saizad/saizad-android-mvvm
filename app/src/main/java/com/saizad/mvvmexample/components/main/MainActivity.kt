package com.saizad.mvvmexample.components.main

import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.saizad.mvvm.components.SaizadBaseViewModel
import com.saizad.mvvm.utils.addToDisposable
import com.saizad.mvvm.utils.startActivityClear
import com.saizad.mvvmexample.ApiRequestCodes
import com.saizad.mvvmexample.R
import com.saizad.mvvmexample.components.MVVMExampleActivity
import com.saizad.mvvmexample.components.auth.AuthActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : MVVMExampleActivity<MainActivityViewModel>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_drawer)
    }

    override val viewModelClassType: Class<MainActivityViewModel>
        get() = MainActivityViewModel::class.java


    override fun navController(): NavController {
        return findNavController(R.id.main_nav)
    }

    override fun onViewReady() {
        super.onViewReady()
        viewModel().currentUserType
            .loggedOutUser()
            .observe(this, Observer {
                startActivityClear<AuthActivity>()
            })
    }
}