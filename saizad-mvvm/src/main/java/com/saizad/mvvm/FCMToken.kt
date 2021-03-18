package com.saizad.mvvm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asFlow
import com.shopify.livedataktx.filter
import com.shopify.livedataktx.map
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FCMToken @Inject constructor(private val dataStoreWrapper: DataStoreWrapper) {

    val tokenLiveData = MutableLiveData<String?>()
    private var token: String? = null

    companion object {
        private const val KEY_FCM_TOKEN = "fcm_token"
    }

    init {

        GlobalScope.launch {
            tokenLiveData
                .asFlow()
                .drop(1)
                .collect {
                    token = it
                    if (it == null) {
                        dataStoreWrapper.remove(KEY_FCM_TOKEN)
                    } else {
                        dataStoreWrapper.putObject(KEY_FCM_TOKEN, it as Any)
                    }
                }
        }

        GlobalScope.launch {
            tokenLiveData.postValue(
                dataStoreWrapper.getValue(KEY_FCM_TOKEN).firstOrNull()
            )
        }

    }

    fun putToken(fcmToken: String?) {
        tokenLiveData.postValue(fcmToken)
    }

    val fcmToken: String?
        get() = token

    fun observeFcmToken(): LiveData<String> {
        return tokenLiveData.filter { !it.isNullOrEmpty() }.map { it!! }
    }

    fun observeFcmTokenRemoved(): LiveData<Void?> {
        return tokenLiveData.filter { it.isNullOrEmpty() }.map { null }
    }


}