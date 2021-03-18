package com.saizad.mvvm

import android.app.Application
import com.google.gson.Gson
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import sa.zad.easyretrofit.EasyRetrofit
import sa.zad.easyretrofit.EasyRetrofitClient

class SaizadEasyRetrofit(
    application: Application,
    private val currentUser: CurrentUserType<*>,
    private val gson: Gson,
    private val domainUrl: String,
    private val isDebugMode: Boolean = false
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