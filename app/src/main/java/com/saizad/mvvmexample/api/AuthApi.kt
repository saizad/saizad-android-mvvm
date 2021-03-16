package com.saizad.mvvmexample.api

import android.graphics.pdf.PdfDocument
import com.saizad.mvvm.model.DataModel
import com.saizad.mvvm.model.LoginBody
import com.saizad.mvvm.model.PageDataModel
import com.saizad.mvvmexample.models.LoginResponse
import com.saizad.mvvmexample.models.ReqResUser
import retrofit2.http.*
import sa.zad.easyretrofit.observables.NeverErrorObservable

interface AuthApi {

    @POST("api/login/")
    fun login(@Body loginBody: LoginBody): NeverErrorObservable<LoginResponse>

    @GET("api/users/{user}")
    fun user(@Path("user") user: Int): NeverErrorObservable<DataModel<ReqResUser>>

}