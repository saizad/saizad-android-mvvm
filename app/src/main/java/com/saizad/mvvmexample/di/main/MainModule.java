package com.saizad.mvvmexample.di.main;

import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.saizad.mvvm.ActivityResult;
import com.saizad.mvvm.SaizadEasyRetrofit;
import com.saizad.mvvm.CurrentUserType;
import com.saizad.mvvm.FCMToken;
import com.saizad.mvvm.NotifyOnce;
import com.saizad.mvvm.ObjectPreference;
import com.saizad.mvvmexample.api.MainApi;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;
import io.reactivex.subjects.BehaviorSubject;
import sa.zad.easypermission.PermissionManager;

@Module
public class MainModule {

    @Provides
    static MainEnvironment providesAuthEnvironment(FCMToken fcmToken, MainApi mainApi, PermissionManager permissionManager, CurrentUserType currentUser, BehaviorSubject<ActivityResult<?>> navigationFragmentResult, @Named("notification") BehaviorSubject<NotifyOnce<?>> notifyOnceBehaviorSubject) {
        return new MainEnvironment(fcmToken, mainApi, navigationFragmentResult, currentUser, permissionManager, notifyOnceBehaviorSubject);
    }

    @Provides
    static MainApi providesMainApi(SaizadEasyRetrofit saizadEasyRetrofit) {
        return saizadEasyRetrofit.provideRetrofit().create(MainApi.class);
    }

    @Provides
    static ObjectPreference providesObjectPreference(SharedPreferences sharedPreferences, Gson gson) {
        return new ObjectPreference(sharedPreferences, gson);
    }

}

