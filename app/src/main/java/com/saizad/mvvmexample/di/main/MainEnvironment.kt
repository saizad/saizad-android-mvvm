package com.saizad.mvvmexample.di.main

import com.saizad.mvvm.*
import com.saizad.mvvmexample.api.MainApi
import io.reactivex.subjects.BehaviorSubject
import sa.zad.easypermission.PermissionManager
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton

class MainEnvironment @Inject constructor(
    val api: MainApi,
    fcmToken: FCMToken,
    behaviorSubject: BehaviorSubject<ActivityResult<*>>,
    currentUser: CurrentUserType<*>,
    permissionManager: PermissionManager,
    notifyOnceBehaviorSubject: BehaviorSubject<NotifyOnce<*>>
) : Environment(
    fcmToken,
    behaviorSubject,
    currentUser,
    notifyOnceBehaviorSubject,
    permissionManager
)