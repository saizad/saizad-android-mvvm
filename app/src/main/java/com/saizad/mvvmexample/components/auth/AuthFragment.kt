package com.saizad.mvvmexample.components.auth

import com.saizad.mvvmexample.api.AuthApi
import com.saizad.mvvmexample.components.MVVMExampleFragment
import com.saizad.mvvmexample.di.auth.AuthEnvironment
import javax.inject.Inject

abstract class AuthFragment<V : AuthViewModel> : MVVMExampleFragment<V>() {


}