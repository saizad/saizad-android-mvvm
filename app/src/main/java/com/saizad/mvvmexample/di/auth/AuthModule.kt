package com.saizad.mvvmexample.di.auth

import com.saizad.mvvm.*
import com.saizad.mvvmexample.MVVMExampleCurrentUser
import com.saizad.mvvmexample.api.AuthApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.reactivex.subjects.BehaviorSubject
import sa.zad.easypermission.PermissionManager
import javax.inject.Named

@Module
@InstallIn(SingletonComponent::class)
object AuthModule {

    @Provides
    fun providesAuthEnvironment(
        authApi: AuthApi,
        fcmToken: FCMToken,
        currentUser: MVVMExampleCurrentUser,
        navigationFragmentResult: BehaviorSubject<ActivityResult<*>>,
        @Named("notification") notifyOnceBehaviorSubject: BehaviorSubject<NotifyOnce<*>>,
        permissionManager: PermissionManager
    ): AuthEnvironment {
        return AuthEnvironment(
            authApi,
            fcmToken,
            navigationFragmentResult,
            currentUser,
            notifyOnceBehaviorSubject,
            permissionManager
        )
    }


    @Provides
    fun providesAuthApi(easyRetrofit: SaizadEasyRetrofit): AuthApi {
        return easyRetrofit.provideRetrofit().create(AuthApi::class.java)
    }
}