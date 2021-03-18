package com.saizad.mvvm.di

import android.app.Application
import android.content.SharedPreferences
import android.preference.PreferenceManager
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.FieldNamingPolicy
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.saizad.mvvm.ActivityResult
import com.saizad.mvvm.NotifyOnce
import com.saizad.mvvm.SaizadLocation
import com.shopify.livedataktx.SingleLiveData
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.reactivex.subjects.BehaviorSubject
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object BaseDiAppModule {

    @Singleton
    @Provides
    fun providesAppLocation(application: Application): SaizadLocation {
        return SaizadLocation(application)
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
}