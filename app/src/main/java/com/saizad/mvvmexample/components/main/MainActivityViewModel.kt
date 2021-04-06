package com.saizad.mvvmexample.components.main

import androidx.lifecycle.SavedStateHandle
import com.saizad.mvvmexample.di.auth.AuthEnvironment
import com.saizad.mvvmexample.di.main.MainEnvironment
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    environment: MainEnvironment
) : MainViewModel(environment)