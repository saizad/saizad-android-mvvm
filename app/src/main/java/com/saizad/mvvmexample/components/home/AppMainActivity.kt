package com.saizad.mvvmexample.components.home

import com.saizad.mvvm.SaizadLocation
import com.saizad.mvvm.Environment
import com.saizad.mvvm.ViewModelProviderFactory
import com.saizad.mvvm.components.DrawerMainActivity
import com.saizad.mvvmexample.MainEnvironment
import javax.inject.Inject

class AppMainActivity: DrawerMainActivity<AppMainViewModel>() {

    @Inject
    lateinit var saizadLocation: SaizadLocation

    @Inject
    lateinit var mainEnvironment: MainEnvironment

    @Inject
    lateinit var viewModelProviderFactory: ViewModelProviderFactory
    
    override fun environment(): Environment {
        return mainEnvironment
    }

    override fun viewModelProviderFactory(): ViewModelProviderFactory {
        return viewModelProviderFactory
    }

    override fun getViewModelClassType(): Class<AppMainViewModel> {
        return AppMainViewModel::class.java
    }

    override fun appLocation(): SaizadLocation {
        return saizadLocation
    }
}