package com.saizad.mvvmexample.di.auth;

import com.saizad.mvvm.ActivityResult;
import com.saizad.mvvm.SaizadEasyRetrofit;
import com.saizad.mvvm.CurrentUserType;
import com.saizad.mvvm.FCMToken;
import com.saizad.mvvm.NotifyOnce;
import com.saizad.mvvmexample.api.AuthApi;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;
import io.reactivex.subjects.BehaviorSubject;
import sa.zad.easypermission.PermissionManager;

@Module
public class AuthModule {


    @Provides
    static AuthEnvironment providesAuthEnvironment(FCMToken fcmToken, AuthApi authApi, CurrentUserType currentUser, BehaviorSubject<ActivityResult<?>> navigationFragmentResult, @Named("notification") BehaviorSubject<NotifyOnce<?>> notifyOnceBehaviorSubject, PermissionManager permissionManager) {
        return new AuthEnvironment(fcmToken, authApi, navigationFragmentResult, currentUser, notifyOnceBehaviorSubject, permissionManager);
    }

    @Provides
    static AuthApi providesAuthApi(SaizadEasyRetrofit easyRetrofit) {
        return easyRetrofit.provideRetrofit().create(AuthApi.class);
    }
}
