package com.saizad.mvvmexample

import android.content.SharedPreferences
import com.google.gson.Gson
import com.saizad.mvvm.CurrentUserType
import com.saizad.mvvmexample.models.ReqResUser

class MVVMExampleCurrentUser(sharedPreferences: SharedPreferences, gson: Gson) :
    CurrentUserType<ReqResUser>(ReqResUser.USER, sharedPreferences, gson) {

    override fun getClassType(): Class<ReqResUser> {
        return ReqResUser::class.java
    }

    override fun getToken(): String? {
        return null
    }
}