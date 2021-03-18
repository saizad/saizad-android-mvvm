package com.saizad.mvvmexample.di.main

import com.saizad.mvvm.ActivityResult
import com.saizad.mvvm.FCMToken
import com.saizad.mvvm.NotifyOnce
import com.saizad.mvvm.SaizadEasyRetrofit
import com.saizad.mvvmexample.MVVMExampleCurrentUser
import com.saizad.mvvmexample.api.MainApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.reactivex.subjects.BehaviorSubject
import sa.zad.easypermission.PermissionManager
import javax.inject.Named

@Module
@InstallIn(SingletonComponent::class)
object MainModule {
    
    @Provides
    fun providesAuthEnvironment(
        mainApi: MainApi,
        fcmToken: FCMToken,
        permissionManager: PermissionManager,
        currentUser: MVVMExampleCurrentUser,
        navigationFragmentResult: BehaviorSubject<ActivityResult<*>>,
        @Named("notification") notifyOnceBehaviorSubject: BehaviorSubject<NotifyOnce<*>>
    ): MainEnvironment {
        return MainEnvironment(
            mainApi,
            fcmToken,
            navigationFragmentResult,
            currentUser,
            permissionManager,
            notifyOnceBehaviorSubject
        )
    }
    
    @Provides
    fun providesMainApi(saizadEasyRetrofit: SaizadEasyRetrofit): MainApi {
        return saizadEasyRetrofit.provideRetrofit().create(MainApi::class.java)
    }
}