package com.saizad.mvvmexample.components.auth.blank

import com.saizad.mvvmexample.components.auth.AuthViewModel
import com.saizad.mvvmexample.di.auth.AuthEnvironment
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class BlankViewModel @Inject constructor(
    authEnvironment: AuthEnvironment
) : AuthViewModel(authEnvironment)