package com.saizad.mvvm

import io.reactivex.subjects.BehaviorSubject
import sa.zad.easypermission.PermissionManager

abstract class Environment(
    val fcmToken: FCMToken,
    val activityResultBehaviorSubject: BehaviorSubject<ActivityResult<*>>,
    val currentUser: CurrentUserType<*>,
    val notificationBehaviorSubject: BehaviorSubject<NotifyOnce<*>>,
    val permissionManager: PermissionManager
)