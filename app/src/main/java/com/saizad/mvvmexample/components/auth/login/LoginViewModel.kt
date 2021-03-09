package com.saizad.mvvmexample.components.auth.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.saizad.mvvm.model.LoginBody
import com.saizad.mvvmexample.components.auth.AuthViewModel
import com.saizad.mvvmexample.di.auth.AuthEnvironment
import com.saizad.mvvmexample.models.ApiError
import com.saizad.mvvmexample.models.LoginResponse
import com.shopify.livedataktx.SingleLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    environment: AuthEnvironment,
    savedStateHandle: SavedStateHandle
) : AuthViewModel(environment, savedStateHandle) {

    private val form by lazy {
        LoginBody.EmailLoginForm("eve.holt@reqres.in")
    }

    val loginFormLiveData: MutableLiveData<LoginBody.EmailLoginForm> = MutableLiveData()

    init {
        val random = java.util.UUID.randomUUID().toString().substring(0, 10)
        form.passwordField.field = random
        loginFormLiveData.postValue(form)
    }

    fun login(): LiveData<LoginResponse> {
        return mVVMExampleRequest(api.login(form.requiredBuild()), 1)
    }
}