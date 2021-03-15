package com.saizad.mvvmexample.api

import com.saizad.mvvm.model.DataModel
import com.saizad.mvvm.model.IntPageDataModel
import com.saizad.mvvmexample.models.ReqResUser
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import sa.zad.easyretrofit.observables.NeverErrorObservable

interface MainApi {

    @GET("api/users/")
    fun users(@Query("page") page: Int?): NeverErrorObservable<IntPageDataModel<ReqResUser>>

    @GET("api/users/{user}")
    fun user(@Path("user") user: Int): NeverErrorObservable<DataModel<ReqResUser>>

    @GET("api/users/")
    fun delayedResponse(@Query("delay") delaySecs: Int): NeverErrorObservable<DataModel<List<ReqResUser>>>
}