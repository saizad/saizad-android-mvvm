package com.saizad.mvvmexample.components

import com.saizad.mvvm.SaizadLocation
import com.saizad.mvvm.components.SaizadBaseActivity
import sa.zad.easypermission.PermissionManager
import javax.inject.Inject

abstract class MVVMExampleActivity<VM : MVVMExampleViewModel> : SaizadBaseActivity<VM>() {

    override fun onSupportNavigateUp() = navController().navigateUp()

    @Inject
    lateinit var gpsLocation: SaizadLocation

    @Inject
    lateinit var permissionManager: PermissionManager

    override fun permissionManager(): PermissionManager {
        return permissionManager
    }

    final override fun appLocation(): SaizadLocation {
        return gpsLocation
    }
}