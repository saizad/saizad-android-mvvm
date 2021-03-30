package com.saizad.mvvmexample.components.main

import com.saizad.mvvm.SaizadLocation
import com.saizad.mvvm.pager.BasePage
import com.saizad.mvvmexample.MVVMExampleCurrentUser
import sa.zad.easypermission.PermissionManager
import javax.inject.Inject

abstract class MainPageFragment<VM : MainPageViewModel> : BasePage<VM>() {
    
    @Inject
    lateinit var permissionManager: PermissionManager

    @Inject
    lateinit var gpsLocation: SaizadLocation

    @Inject
    lateinit var mvvmExampleCurrentUser: MVVMExampleCurrentUser
    
    override fun appLocation(): SaizadLocation {
        return gpsLocation
    }

    override fun permissionManager(): PermissionManager {
        return permissionManager
    }

}