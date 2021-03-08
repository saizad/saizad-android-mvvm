package com.saizad.mvvmexample.components.auth

import androidx.lifecycle.SavedStateHandle
import com.saizad.mvvmexample.components.MVVMExampleViewModel
import com.saizad.mvvmexample.di.auth.AuthEnvironment
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

abstract class AuthViewModel constructor(
    authEnvironment: AuthEnvironment,
    savedStateHandle: SavedStateHandle
) : MVVMExampleViewModel(authEnvironment, savedStateHandle)