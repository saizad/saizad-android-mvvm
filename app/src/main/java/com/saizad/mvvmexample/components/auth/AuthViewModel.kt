package com.saizad.mvvmexample.components.auth

import com.saizad.mvvmexample.components.MVVMExampleViewModel
import com.saizad.mvvmexample.di.auth.AuthEnvironment

abstract class AuthViewModel(authEnvironment: AuthEnvironment) :
    MVVMExampleViewModel(authEnvironment)