package com.saizad.mvvmexample.di.auth

import com.saizad.mvvm.*
import com.saizad.mvvmexample.api.AuthApi
import io.reactivex.subjects.BehaviorSubject
import sa.zad.easypermission.PermissionManager
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton

class AuthEnvironment @Inject constructor(
    val api: AuthApi,
    fcmToken: FCMToken,
    behaviorSubject: BehaviorSubject<ActivityResult<*>>,
    currentUser: CurrentUserType<*>,
    notifyOnceBehaviorSubject: BehaviorSubject<NotifyOnce<*>>,
    permissionManager: PermissionManager
) : Environment(
    fcmToken, behaviorSubject, currentUser, notifyOnceBehaviorSubject, permissionManager
)