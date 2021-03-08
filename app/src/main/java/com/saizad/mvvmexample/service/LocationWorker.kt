package com.saizad.mvvmexample.service

import android.content.Context
import android.location.Location
import androidx.work.WorkerParameters
import com.saizad.mvvm.SaizadLocation
import com.saizad.mvvm.service.BaseLocationWorker
import com.saizad.mvvmexample.RequestCodes
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import sa.zad.easypermission.PermissionManager

class LocationWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted params: WorkerParameters,
    gpsLocation: SaizadLocation, permissionManager: PermissionManager
) : BaseLocationWorker(
    context,
    params,
    gpsLocation,
    permissionManager.getAppPermission(RequestCodes.LOCATION_PERMISSION_REQUEST_CODE)
) {
    override fun locationDoWork(location: Location): Result {
        return Result.success()
    }
}