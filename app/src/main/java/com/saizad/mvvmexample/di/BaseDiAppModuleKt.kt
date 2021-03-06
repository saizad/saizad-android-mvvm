package com.saizad.mvvmexample.di

import android.Manifest
import android.app.Application
import android.content.SharedPreferences
import android.preference.PreferenceManager
import com.google.gson.FieldNamingPolicy
import com.google.gson.Gson
import com.google.gson.GsonBuilder
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
open class BaseDiAppModuleKt {

    fun retrofit(
        application: Application,
        currentUser: CurrentUserType<*>,
        gson: Gson
    ): SaizadEasyRetrofit {
        return SaizadEasyRetrofit(application, currentUser, gson, domainURL())
    }

    @Singleton
    @Provides
    fun providesRetrofit(
        application: Application,
        currentUser: CurrentUserType<*>,
        gson: Gson
    ): SaizadEasyRetrofit {
        return retrofit(application, currentUser, gson)
    }

    open fun domainURL(): String? {
        return BuildConfig.DOMAIN_URL
    }

    @Singleton
    @Provides
    fun providesAppLocation(application: Application): SaizadLocation {
        return SaizadLocation(application)
    }

    @Singleton
    @Provides
    fun providesFCMToken(sharedPreferences: SharedPreferences, gson: Gson): FCMToken {
        return FCMToken(sharedPreferences, gson)
    }

    @Singleton
    @Provides
    fun providesSharedPreferences(application: Application): SharedPreferences {
        return PreferenceManager.getDefaultSharedPreferences(application)
    }

    @Singleton
    @Provides
    fun provideGson(): Gson {
        val gsonBuilder = GsonBuilder().serializeNulls()
        gsonBuilder.setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
        return gsonBuilder.create()
    }

    @Singleton
    @Provides
    fun provideFragmentNavigationResult(): BehaviorSubject<ActivityResult<*>> {
        return BehaviorSubject.create()
    }

    @Singleton
    @Provides
    @Named("notification")
    fun notificationResultBehaviorSubject(): BehaviorSubject<NotifyOnce<*>> {
        return BehaviorSubject.create()
    }

    @Singleton
    @Provides
    fun providesEnvironment(
        fcmToken: FCMToken,
        currentUser: CurrentUserType<*>,
        navigationFragmentResult: BehaviorSubject<ActivityResult<*>>,
        @Named("notification") notifyOnceBehaviorSubject: BehaviorSubject<NotifyOnce<*>>,
        permissionManager: PermissionManager
    ): Environment {
        return Environment(
            fcmToken,
            navigationFragmentResult,
            currentUser,
            notifyOnceBehaviorSubject,
            permissionManager
        )
    }

    @Singleton
    @Provides
    open fun currentUser(sharedPreferences: SharedPreferences, gson: Gson): CurrentUserType<*> {
        return MVVMExampleCurrentUser(sharedPreferences, gson)
    }

    @Singleton
    @Provides
    protected open fun providesPermissionManager(sharedPreferences: SharedPreferences): PermissionManager {
        return PermissionManager(
            storagePermission(sharedPreferences),
            locationPermission(sharedPreferences)
        )
    }

     fun storagePermission(sharedPreferences: SharedPreferences): AppPermission {
        return AppPermissionImp(
            RequestCodes.STORAGE_PERMISSION_REQUEST_CODE, arrayOf(
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ), AppModule.MAX_REQUEST, sharedPreferences
        )
    }

     fun locationPermission(sharedPreferences: SharedPreferences): AppPermission {
        return AppPermissionImp(
            RequestCodes.LOCATION_PERMISSION_REQUEST_CODE, arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            ), AppModule.MAX_REQUEST, sharedPreferences
        )
    }

    @Singleton
    @Provides
    protected open fun providesBackgroundApi(saizadEasyRetrofit: SaizadEasyRetrofit): BackgroundApi {
        return saizadEasyRetrofit.provideRetrofit().create(BackgroundApi::class.java)
    }
}