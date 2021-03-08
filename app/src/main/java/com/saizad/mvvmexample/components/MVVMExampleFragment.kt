package com.saizad.mvvmexample.components

import com.saizad.mvvm.SaizadLocation
import com.saizad.mvvm.components.SaizadBaseFragment
import com.saizad.mvvmexample.MVVMExampleCurrentUser
import sa.zad.easypermission.PermissionManager
import javax.inject.Inject

abstract class MVVMExampleFragment<VM : MVVMExampleViewModel> :
    SaizadBaseFragment<VM>() {

    @Inject
    lateinit var gpsLocation: SaizadLocation

    @Inject
    lateinit var currentUserType: MVVMExampleCurrentUser

    @Inject
    lateinit var permissionManager: PermissionManager

    @Inject
    lateinit var location: SaizadLocation

    override fun appLocation(): SaizadLocation {
        return location
    }

    override fun permissionManager(): PermissionManager {
        return permissionManager
    }

}