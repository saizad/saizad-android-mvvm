package com.saizad.mvvmexample.components.main

import androidx.lifecycle.SavedStateHandle
import com.saizad.mvvmexample.MVVMExampleCurrentUser
import com.saizad.mvvmexample.RequestCodes
import com.saizad.mvvmexample.api.MainApi
import com.saizad.mvvmexample.components.MVVMExampleViewModel
import com.saizad.mvvmexample.di.auth.AuthEnvironment
import com.saizad.mvvmexample.di.main.MainEnvironment
import com.saizad.mvvmexample.models.ReqResUser
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import javax.inject.Named

abstract class MainViewModel constructor(
    environment: MainEnvironment,
    savedStateHandle: SavedStateHandle
) : MVVMExampleViewModel(environment, savedStateHandle){

    val api: MainApi = environment.api
    var currentUserType: MVVMExampleCurrentUser = environment.currentUser as MVVMExampleCurrentUser

    override fun onViewCreated() {
        onNavigationResult(RequestCodes.USER, ReqResUser::class.java)
            .observeForever {
                currentUserType.refresh(it)
            }

        super.onViewCreated()
    }
}