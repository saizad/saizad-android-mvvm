package com.saizad.mvvmexample

import android.util.Log
import androidx.hilt.work.HiltWorkerFactory
import androidx.lifecycle.Observer
import androidx.work.Configuration
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkInfo
import androidx.work.WorkManager
import com.saizad.mvvm.CurrentUserType
import com.saizad.mvvm.SaizadApplication
import com.saizad.mvvmexample.service.LocationWorker
import dagger.hilt.android.HiltAndroidApp
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltAndroidApp
class MVVMExampleApplication : SaizadApplication() {
    
    @Inject
    lateinit var currentUserType: MVVMExampleCurrentUser
    
    @Inject
    lateinit var workerFactory: HiltWorkerFactory
    
    override fun onCreate() {
        super.onCreate()
        WorkManager.initialize(
            this,
            Configuration.Builder().setWorkerFactory(workerFactory).build()
        )
    }

    fun cancelLocationWorker() {
        cancelWorker(LOCATION_UPDATE_TAG)
    }

    fun cancelWorker(tag: String) {
        WorkManager.getInstance(this).cancelAllWorkByTag(tag)
        Log.d(tag, "Cancelled")
    }

    fun runLocationWorker() {
        Log.d(LOCATION_UPDATE_TAG, "RunWorkManager")
        val oneTimeWorkRequest =
            OneTimeWorkRequest.Builder(LocationWorker::class.java)
                .addTag(LOCATION_UPDATE_TAG)
                .setInitialDelay(5, TimeUnit.SECONDS)
                .build()
        WorkManager.getInstance(this).beginWith(oneTimeWorkRequest).enqueue()
        val observer =
            Observer { workInfo: WorkInfo ->
                Log.d(
                    LOCATION_UPDATE_TAG,
                    workInfo.toString()
                )
                if (workInfo.state == WorkInfo.State.SUCCEEDED) {
                    Log.d(
                        LOCATION_UPDATE_TAG,
                        "SUCCEEDED"
                    )
                    runLocationWorker()
                }
            }
        WorkManager.getInstance(this).getWorkInfoByIdLiveData(oneTimeWorkRequest.id)
            .observeForever(observer)
    }

    companion object {
        const val LOCATION_UPDATE_TAG = "location_update"
    }

}