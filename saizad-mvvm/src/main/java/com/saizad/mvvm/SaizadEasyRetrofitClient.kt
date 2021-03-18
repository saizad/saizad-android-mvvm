package com.saizad.mvvm

import android.app.Application
import com.facebook.stetho.okhttp3.StethoInterceptor
import com.itkacher.okhttpprofiler.OkHttpProfilerInterceptor
import com.sa.easyandroidform.ObjectUtils
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import sa.zad.easyretrofit.CachePolicy
import sa.zad.easyretrofit.EasyRetrofitClient
import java.net.URLDecoder

class SaizadEasyRetrofitClient(
    application: Application,
    private val currentUser: CurrentUserType<*>,
    private val isDebugMode: Boolean = false
) : EasyRetrofitClient(application) {

    override fun builderReady(builder: OkHttpClient.Builder): OkHttpClient.Builder {
        builder.addInterceptor(getAuthInterceptor(currentUser))
        if (isDebugMode) {
            builder.addNetworkInterceptor(StethoInterceptor())
            builder.addNetworkInterceptor(TextToJsonInterceptor())
            builder.addInterceptor(OkHttpProfilerInterceptor())
        }
        return builder
    }

    override fun loggingLevel(): HttpLoggingInterceptor.Level {
        return if (isDebugMode) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE
    }

    override fun cacheStale(cachePolicy: Int): Long {
        return if (cachePolicy == CachePolicy.LOCAL_IF_FRESH) {
            60L
        } else super.cacheStale(cachePolicy)
    }

    companion object {
        private fun getAuthInterceptor(currentUser: CurrentUserType<*>): Interceptor {
            return Interceptor { chain: Interceptor.Chain ->
                var request = chain.request()
                val decode = URLDecoder.decode(request.url.toString(), "UTF-8")
                request = request.newBuilder().url(decode).build()
                val token = currentUser.token
                if (ObjectUtils.isNotNull(token)) {
                    request =
                        request.newBuilder().header("Authorization", "Bearer $token").build()
                }
                chain.proceed(request)
            }
        }
    }

}