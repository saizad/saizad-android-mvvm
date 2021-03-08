package com.saizad.mvvmexample.components.auth.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import com.saizad.mvvm.model.UserInfo
import com.saizad.mvvmexample.components.auth.AuthViewModel
import com.saizad.mvvmexample.di.auth.AuthEnvironment
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    authEnvironment: AuthEnvironment,
    savedStateHandle: SavedStateHandle
) : AuthViewModel(authEnvironment, savedStateHandle) {

    private val _toastLiveData: MutableLiveData<String> = MutableLiveData()
    val toastLiveData: LiveData<String> get() = _toastLiveData
    private val userInfo: UserInfo = savedStateHandle.get("user")!!

    init {
        _toastLiveData.postValue(userInfo.userName)
    }
}