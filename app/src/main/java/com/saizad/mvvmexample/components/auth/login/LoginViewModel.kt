package com.saizad.mvvmexample.components.auth.login

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import com.saizad.mvvm.enums.DataState
import com.saizad.mvvm.model.DataModel
import com.saizad.mvvm.model.LoginBody
import com.saizad.mvvmexample.ApiRequestCodes
import com.saizad.mvvmexample.components.auth.AuthViewModel
import com.saizad.mvvmexample.di.auth.AuthEnvironment
import com.saizad.mvvmexample.models.ApiError
import com.saizad.mvvmexample.models.LoginResponse
import com.saizad.mvvmexample.models.ReqResUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
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

    fun login(requestId: Int = ApiRequestCodes.LOGIN): Flow<DataState<LoginResponse>> {
        return flowData(api.login(form.requiredBuild()), requestId)
    }

    fun user(user: Int, requestId: Int = ApiRequestCodes.USER): Flow<DataState<DataModel<ReqResUser>>> {
        return liveData(api.user(user), requestId)
    }
}