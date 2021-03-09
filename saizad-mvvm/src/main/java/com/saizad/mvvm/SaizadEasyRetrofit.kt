package com.saizad.mvvm

import android.app.Application
import com.google.gson.Gson
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import sa.zad.easyretrofit.EasyRetrofit
import sa.zad.easyretrofit.EasyRetrofitClient

class SaizadEasyRetrofit(
    protected val application: Application,
    protected val currentUser: CurrentUserType<*>,
    protected val gson: Gson,
    protected val domainUrl: String,
    val isDebugMode: Boolean = false
) : EasyRetrofit(application) {

    override fun retrofitBuilderReady(retrofitBuilder: Retrofit.Builder): Retrofit.Builder {
        return retrofitBuilder
            .baseUrl(domainUrl)
    }

    override fun addConverterFactory(): Converter.Factory {
        return GsonConverterFactory.create(gson)
    }

    override fun easyRetrofitClient(): EasyRetrofitClient {
        return SaizadEasyRetrofitClient(provideApplication(), currentUser, isDebugMode)
    }

}