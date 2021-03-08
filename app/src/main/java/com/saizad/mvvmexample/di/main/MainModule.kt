package com.saizad.mvvmexample.di.main

import android.content.SharedPreferences
import com.google.gson.Gson
import com.saizad.mvvm.*
import com.saizad.mvvmexample.api.MainApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.reactivex.subjects.BehaviorSubject
import sa.zad.easypermission.PermissionManager
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object MainModule {
    
    @Singleton
    @Provides
    fun providesAuthEnvironment(
        mainApi: MainApi,
        fcmToken: FCMToken,
        permissionManager: PermissionManager,
        currentUser: CurrentUserType<*>,
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
    
    @Singleton
    @Provides
    fun providesMainApi(saizadEasyRetrofit: SaizadEasyRetrofit): MainApi {
        return saizadEasyRetrofit.provideRetrofit().create(MainApi::class.java)
    }
    
    @Singleton
    @Provides
    fun providesObjectPreference(
        sharedPreferences: SharedPreferences, gson: Gson
    ): ObjectPreference {
        return ObjectPreference(sharedPreferences, gson)
    }
}