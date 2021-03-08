package com.saizad.mvvmexample.components.main

import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.saizad.mvvmexample.R
import com.saizad.mvvmexample.components.MVVMExampleActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : MVVMExampleActivity<MainActivityViewModel>() {

    override fun onCreate(savedInstanceState: Bundle?) {
//        setTheme(R.style.MainTheme)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_drawer)
    }

    override val viewModelClassType: Class<MainActivityViewModel>
        get() = MainActivityViewModel::class.java


    override fun navController(): NavController {
        return findNavController(R.id.main_nav)
    }
}