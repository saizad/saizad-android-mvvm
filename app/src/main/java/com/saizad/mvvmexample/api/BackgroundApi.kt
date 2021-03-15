package com.saizad.mvvmexample.api

import com.saizad.mvvm.model.DataModel
import com.saizad.mvvm.model.FCMTokenBody
import com.saizad.mvvm.model.NameModel
import retrofit2.http.Body
import retrofit2.http.POST
import sa.zad.easyretrofit.observables.NeverErrorObservable

interface BackgroundApi {
    @POST("fcm_register/")
    fun fcmRegister(@Body fcmTokenBody: FCMTokenBody): NeverErrorObservable<DataModel<NameModel>>
}