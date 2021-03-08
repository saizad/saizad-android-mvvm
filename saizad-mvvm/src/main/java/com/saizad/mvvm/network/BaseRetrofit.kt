package com.saizad.mvvm.network

import android.app.Application
import com.google.gson.Gson
import com.saizad.mvvm.CurrentUserType
import com.saizad.mvvm.SaizadEasyRetrofit

class BaseRetrofit(
    val currentUser: CurrentUserType<*>,
    private val baseURL: String,
    val application: Application,
    val gson: Gson
): SaizadEasyRetrofit(application, currentUser, gson, baseURL)