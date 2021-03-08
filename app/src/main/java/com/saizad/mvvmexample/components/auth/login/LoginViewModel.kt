package com.saizad.mvvmexample.components.auth.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import com.saizad.mvvm.model.LoginBody
import com.saizad.mvvmexample.components.auth.AuthViewModel
import com.saizad.mvvmexample.di.auth.AuthEnvironment
import com.saizad.mvvmexample.models.LoginResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    environment: AuthEnvironment,
    savedStateHandle: SavedStateHandle
) : AuthViewModel(environment, savedStateHandle) {

    private val form by lazy {
        LoginBody.EmailLoginForm()
    }

    private val _loginFormLiveData: MutableLiveData<LoginBody.EmailLoginForm> = MutableLiveData()
    val loginFormLiveData: LiveData<LoginBody.EmailLoginForm> get() = _loginFormLiveData

    init {
        val random = java.util.UUID.randomUUID().toString().substring(0,10)
        form.emailField.field = "eve.holt@reqres.in"
        form.passwordField.field = random
        _loginFormLiveData.postValue(form)
    }

    fun login(): LiveData<LoginResponse> {
        return liveDataRequestNoEnvelope(api.login(form.requiredBuild()), 1)
    }
}