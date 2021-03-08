package com.saizad.mvvmexample.components.main.home

import androidx.lifecycle.SavedStateHandle
import com.saizad.mvvmexample.components.main.MainViewModel
import com.saizad.mvvmexample.di.main.MainEnvironment
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    environment: MainEnvironment,
    savedStateHandle: SavedStateHandle
) : MainViewModel(environment, savedStateHandle)