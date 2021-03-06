package com.saizad.mvvm.service;

import android.content.Context;
import android.location.Location;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.WorkerThread;
import androidx.work.Data;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.saizad.mvvm.SaizadLocation;

import sa.zad.easypermission.AppPermission;


abstract public class BaseLocationWorker extends Worker {

    public static final String WORK_COMPLETED = "work_completed";
    protected AppPermission locationPermission;
    protected final SaizadLocation saizadLocation;

    public BaseLocationWorker(@NonNull Context context, @NonNull WorkerParameters workerParameters, SaizadLocation saizadLocation, AppPermission locationPermission) {
        super(context, workerParameters);
        this.locationPermission = locationPermission;
        this.saizadLocation = saizadLocation;
    }

    @NonNull
    @Override
    public final Result doWork() {
        if (!locationPermission.isPermissionGranted(getApplicationContext())) {
            Log.d("BaseLocationWorker", "Permission not granted");
            return success(false);
        }
        try {
            final Location location = saizadLocation.getLocationObservable(new SaizadLocation.Builder().fastInterval(1).interval(1).build(), Looper.getMainLooper()).blockingFirst();
            Log.d("location_update", location.toString());
            return locationDoWork(location);
        }catch (Exception e){
            return success(false);
        }
    }

    @WorkerThread
    protected abstract Result locationDoWork(Location location);

    protected static Result success(boolean success){
        return Result.success(new Data.Builder().putBoolean(WORK_COMPLETED, success).build());
    }
}