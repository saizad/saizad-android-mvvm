package com.saizad.mvvmexample.components;

import com.saizad.mvvm.Environment
import com.saizad.mvvm.SaizadLocation
import com.saizad.mvvm.ViewModelProviderFactory
import com.saizad.mvvm.components.SaizadBaseActivity
import com.saizad.mvvm.di.AssistedFactory
import javax.inject.Inject

abstract class MVVMExampleActivity<VM : MVVMExampleViewModel> : SaizadBaseActivity<VM>() {

    override fun onSupportNavigateUp() = navController().navigateUp()

    @Inject
    lateinit var gpsLocation: SaizadLocation

    @Inject
    lateinit var environment: Environment

    final override fun environment(): Environment {
        return environment
    }

    final override fun appLocation(): SaizadLocation {
        return gpsLocation
    }
}