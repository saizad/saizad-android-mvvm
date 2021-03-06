package com.saizad.mvvmexample;

import android.util.Log;

import androidx.hilt.work.HiltWorkerFactory;
import androidx.lifecycle.Observer;
import androidx.work.Configuration;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;

import com.saizad.mvvm.SaizadApplication;
import com.saizad.mvvmexample.service.LocationWorker;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import dagger.hilt.android.HiltAndroidApp;

@HiltAndroidApp
public class MVVMExampleApplication extends SaizadApplication {

    public static final String LOCATION_UPDATE_TAG = "location_update";
    private static MVVMExampleApplication INSTANCE;

//    @Inject
//    CurrentUserType currentUserType;

    @Inject
    HiltWorkerFactory workerFactory;

    public MVVMExampleApplication() {
        INSTANCE = this;
    }

    public static MVVMExampleApplication getInstance() {
        if (INSTANCE != null) {
            return INSTANCE;
        }
        return new MVVMExampleApplication();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        WorkManager.initialize(this, new Configuration.Builder().setWorkerFactory(workerFactory).build());
    }

    public void cancelLocationWorker() {
        cancelWorker(LOCATION_UPDATE_TAG);
    }

    public void cancelWorker(String tag) {
        WorkManager.getInstance(this).cancelAllWorkByTag(tag);
        Log.d(tag, "Cancelled");
    }

    public void runLocationWorker() {
        Log.d(LOCATION_UPDATE_TAG, "RunWorkManager");
        OneTimeWorkRequest oneTimeWorkRequest = new OneTimeWorkRequest.Builder(LocationWorker.class)
                .addTag(LOCATION_UPDATE_TAG)
                .setInitialDelay(5, TimeUnit.SECONDS)
                .build();

        WorkManager.getInstance(this).beginWith(oneTimeWorkRequest).enqueue();

        final Observer<WorkInfo> observer = workInfo -> {
            Log.d(LOCATION_UPDATE_TAG, workInfo.toString());
            if (workInfo.getState() == WorkInfo.State.SUCCEEDED) {
                Log.d(LOCATION_UPDATE_TAG, "SUCCEEDED");
                runLocationWorker();
            }
        };

        WorkManager.getInstance(this).getWorkInfoByIdLiveData(oneTimeWorkRequest.getId()).observeForever(observer);
    }


//    public MVVMExampleCurrentUser getCurrentUserType() {
//        return (MVVMExampleCurrentUser) currentUserType;
//    }
}
