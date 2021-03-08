package com.saizad.mvvmexample.components.auth

import androidx.lifecycle.SavedStateHandle
import com.saizad.mvvmexample.api.AuthApi
import com.saizad.mvvmexample.components.MVVMExampleViewModel
import com.saizad.mvvmexample.di.auth.AuthEnvironment
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import javax.inject.Named

abstract class AuthViewModel constructor(
    authEnvironment: AuthEnvironment,
    savedStateHandle: SavedStateHandle,
    val api: AuthApi = authEnvironment.api
) : MVVMExampleViewModel(authEnvironment, savedStateHandle)