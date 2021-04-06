package com.saizad.mvvmexample.components.auth

import com.saizad.mvvmexample.api.AuthApi
import com.saizad.mvvmexample.components.MVVMExampleViewModel
import com.saizad.mvvmexample.di.auth.AuthEnvironment

abstract class AuthViewModel constructor(
    authEnvironment: AuthEnvironment,
    val api: AuthApi = authEnvironment.api
) : MVVMExampleViewModel(authEnvironment)