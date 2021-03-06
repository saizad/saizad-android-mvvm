package com.saizad.mvvmexample.service;

import android.content.Context;
import android.location.Location;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.WorkerParameters;

import com.saizad.mvvm.SaizadLocation;
import com.saizad.mvvm.service.BaseLocationWorker;
import com.saizad.mvvmexample.RequestCodes;

import dagger.assisted.Assisted;
import dagger.assisted.AssistedInject;
import sa.zad.easypermission.PermissionManager;

public class LocationWorker extends BaseLocationWorker {

    @AssistedInject
    public LocationWorker(
            @Assisted @NonNull Context context,
            @Assisted @NonNull WorkerParameters params,
            SaizadLocation gpsLocation, PermissionManager permissionManager
    ) {
        super(context, params, gpsLocation, permissionManager.getAppPermission(RequestCodes.LOCATION_PERMISSION_REQUEST_CODE));
    }

    @Override
    protected Result locationDoWork(Location location) {
        Log.d("Location", location.toString());
        return Result.success();
    }
}