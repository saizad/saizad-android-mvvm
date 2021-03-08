package com.saizad.mvvmexample.components

import com.saizad.mvvm.CurrentUserType
import com.saizad.mvvm.Environment
import com.saizad.mvvm.SaizadLocation
import com.saizad.mvvm.pager.BasePage
import javax.inject.Inject

abstract class MVVMExamplePageFragment<VM : MVVMExampleViewModel> : BasePage<VM>() {
    
    @Inject
    lateinit var mainEnvironment: Environment

    @Inject
    lateinit var gpsLocation: SaizadLocation
    
    override fun appLocation(): SaizadLocation {
        return gpsLocation
    }

    override fun environment(): Environment {
        return mainEnvironment
    }

    protected fun currentUser(): CurrentUserType<*> {
        return mainEnvironment.currentUser
    }
}