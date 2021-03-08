package com.saizad.mvvmexample.components

import androidx.lifecycle.SavedStateHandle
import com.saizad.mvvm.Environment
import com.saizad.mvvm.components.SaizadBaseViewModel

abstract class MVVMExampleViewModel(
    environment: Environment,
    savedStateHandle: SavedStateHandle
) : SaizadBaseViewModel(environment, savedStateHandle)