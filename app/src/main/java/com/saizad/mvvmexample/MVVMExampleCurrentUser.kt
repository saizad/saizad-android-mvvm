package com.saizad.mvvmexample

import android.content.SharedPreferences
import com.google.gson.Gson
import com.saizad.mvvm.CurrentUserType
import com.saizad.mvvm.model.UserInfo

class MVVMExampleCurrentUser(sharedPreferences: SharedPreferences, gson: Gson) :
    CurrentUserType<UserInfo>(UserInfo(), sharedPreferences, gson) {

    override fun getClassType(): Class<UserInfo> {
        return UserInfo::class.java
    }

    override fun getToken(): String? {
        return null
    }
}