package com.saizad.mvvmexample.di.main;

import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.saizad.mvvm.ActivityResult;
import com.saizad.mvvm.CurrentUserType;
import com.saizad.mvvm.FCMToken;
import com.saizad.mvvm.NotifyOnce;
import com.saizad.mvvm.ObjectPreference;
import com.saizad.mvvm.SaizadEasyRetrofit;
import com.saizad.mvvmexample.api.MainApi;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;
import io.reactivex.subjects.BehaviorSubject;
import sa.zad.easypermission.PermissionManager;

@Module
@InstallIn(SingletonComponent.class)
public class MainModule {

    @Singleton
    @Provides
    static MainEnvironment providesAuthEnvironment(FCMToken fcmToken, MainApi mainApi, PermissionManager permissionManager, CurrentUserType currentUser, BehaviorSubject<ActivityResult<?>> navigationFragmentResult, @Named("notification") BehaviorSubject<NotifyOnce<?>> notifyOnceBehaviorSubject) {
        return new MainEnvironment(fcmToken, mainApi, navigationFragmentResult, currentUser, permissionManager, notifyOnceBehaviorSubject);
    }

    @Singleton
    @Provides
    static MainApi providesMainApi(SaizadEasyRetrofit saizadEasyRetrofit) {
        return saizadEasyRetrofit.provideRetrofit().create(MainApi.class);
    }

    @Singleton
    @Provides
    static ObjectPreference providesAddPharmacyDraft(SharedPreferences sharedPreferences, Gson gson) {
        return new ObjectPreference(sharedPreferences, gson);
    }

}

