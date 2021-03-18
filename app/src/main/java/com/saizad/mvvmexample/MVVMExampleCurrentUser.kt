package com.saizad.mvvmexample

import com.saizad.mvvm.CurrentUserType
import com.saizad.mvvm.DataStoreWrapper
import com.saizad.mvvmexample.models.ReqResUser
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MVVMExampleCurrentUser @Inject constructor(dataStoreWrapper: DataStoreWrapper) :
    CurrentUserType<ReqResUser>(dataStoreWrapper) {

    override val classType: Class<ReqResUser>
        get() = ReqResUser::class.java

    override val token: String?
        get() = null
}