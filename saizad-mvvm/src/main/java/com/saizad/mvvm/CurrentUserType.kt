package com.saizad.mvvm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asFlow
import androidx.lifecycle.map
import com.shopify.livedataktx.filter
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

abstract class CurrentUserType<U> protected constructor(
    private val dataStoreWrapper: DataStoreWrapper
) {
    val currentUserLiveData = MutableLiveData<U?>()
    private var user: U? = null
    protected abstract val classType: Class<U>

    companion object {
        private const val KEY_USER_INFO = "user_info"
    }

    init {

        GlobalScope.launch {
            currentUserLiveData
                .asFlow()
                .drop(1)
                .collect {
                    user = it
                    dataStoreWrapper.putObject(KEY_USER_INFO, it)
                }
        }

        GlobalScope.launch {
            currentUserLiveData.postValue(
                dataStoreWrapper.getObject(KEY_USER_INFO, classType).firstOrNull()
            )
        }

    }

    open fun login(newUser: U) {
        refresh(newUser)
    }

    suspend fun logout() {
        dataStoreWrapper.removeAll()
        currentUserLiveData.postValue(null)
    }

    fun refresh(freshUser: U) {
        currentUserLiveData.postValue(freshUser)
    }

    fun getUser(): U? {
        return user
    }

    fun exists(): Boolean {
        return getUser() != null
    }

    val isLoggedIn: LiveData<Boolean>
        get() = currentUserLiveData
            .map {
                it != null
            }

    fun loggedInUser(): LiveData<U> {
        return currentUserLiveData
            .filter {
                it != null
            }.map { it!! }
    }

    fun loggedOutUser(): LiveData<Void?> {
        return currentUserLiveData
            .filter {
                it == null
            }.map { null }
    }

    abstract val token: String?

}