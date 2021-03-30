package com.saizad.mvvmexample.components.auth

import androidx.lifecycle.SavedStateHandle
import com.saizad.mvvmexample.di.auth.AuthEnvironment
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AuthActivityViewModel @Inject constructor(
    authEnvironment: AuthEnvironment,
    savedStateHandle: SavedStateHandle
) : AuthViewModel(authEnvironment, savedStateHandle)