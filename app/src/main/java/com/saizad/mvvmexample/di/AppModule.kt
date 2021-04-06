package com.saizad.mvvmexample.di

import android.Manifest
import android.app.Application
import android.content.SharedPreferences
import com.google.gson.Gson
import com.saizad.mvvm.*
import com.saizad.mvvmexample.BuildConfig
import com.saizad.mvvmexample.MVVMExampleCurrentUser
import com.saizad.mvvmexample.RequestCodes
import com.saizad.mvvmexample.api.BackgroundApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.reactivex.subjects.BehaviorSubject
import sa.zad.easypermission.AppPermission
import sa.zad.easypermission.AppPermissionImp
import sa.zad.easypermission.PermissionManager
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    private const val MAX_REQUEST = 3

    @Singleton
    @Provides
    fun providesRetrofit(
        application: Application, currentUser: MVVMExampleCurrentUser, gson: Gson
    ): SaizadEasyRetrofit {
        return SaizadEasyRetrofit(application, currentUser, gson, BuildConfig.DOMAIN_URL, BuildConfig.DEBUG)
    }

    @Singleton
    @Provides
    fun providesPermissionManager(sharedPreferences: SharedPreferences): PermissionManager {
        return PermissionManager(
            storagePermission(sharedPreferences),
            locationPermission(sharedPreferences)
        )
    }

    private fun storagePermission(sharedPreferences: SharedPreferences): AppPermission {
        return AppPermissionImp(
            RequestCodes.STORAGE_PERMISSION_REQUEST_CODE, arrayOf(
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ), MAX_REQUEST, sharedPreferences
        )
    }

    private fun locationPermission(sharedPreferences: SharedPreferences): AppPermission {
        return AppPermissionImp(
            RequestCodes.LOCATION_PERMISSION_REQUEST_CODE, arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            ), MAX_REQUEST, sharedPreferences
        )
    }

    @Singleton
    @Provides
    fun providesBackgroundApi(saizadEasyRetrofit: SaizadEasyRetrofit): BackgroundApi {
        return saizadEasyRetrofit.provideRetrofit().create(BackgroundApi::class.java)
    }
}