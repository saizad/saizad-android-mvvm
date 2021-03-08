package com.saizad.mvvmexample.api

import com.saizad.mvvm.model.LoginBody
import com.saizad.mvvmexample.models.LoginResponse
import retrofit2.http.Body
import retrofit2.http.POST
import sa.zad.easyretrofit.observables.NeverErrorObservable

interface AuthApi {

    @POST("api/login/")
    fun login(@Body loginBody: LoginBody): NeverErrorObservable<LoginResponse>

}