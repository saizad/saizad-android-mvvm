package com.saizad.mvvmexample.components

import com.saizad.mvvm.CurrentUserType
import com.saizad.mvvm.Environment
import com.saizad.mvvm.SaizadLocation
import com.saizad.mvvm.components.SaizadBaseFragment
import javax.inject.Inject

abstract class MVVMExampleFragment<VM : MVVMExampleViewModel> :
    SaizadBaseFragment<VM>() {
    
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
        return mainEnvironment.currentUser()
    }
}