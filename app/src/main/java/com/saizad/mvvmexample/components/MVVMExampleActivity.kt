package com.saizad.mvvmexample.components

import com.saizad.mvvm.Environment
import com.saizad.mvvm.SaizadLocation
import com.saizad.mvvm.ViewModelProviderFactory
import com.saizad.mvvm.components.SaizadBaseActivity
import javax.inject.Inject

abstract class MVVMExampleActivity<VM: MVVMExampleViewModel> : SaizadBaseActivity<VM>() {

    override fun onSupportNavigateUp() = navController().navigateUp()

    @Inject
    lateinit var gpsLocation: SaizadLocation

    @Inject
    lateinit var environment: Environment

    @Inject
    lateinit var viewModelProviderFactory: ViewModelProviderFactory

    final override fun environment(): Environment {
        return environment
    }

    final override fun viewModelProviderFactory(): ViewModelProviderFactory {
        return viewModelProviderFactory
    }

    final override fun appLocation(): SaizadLocation {
        return gpsLocation
    }
}