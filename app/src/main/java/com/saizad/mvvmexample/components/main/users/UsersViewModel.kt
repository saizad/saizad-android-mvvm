package com.saizad.mvvmexample.components.main.users

import androidx.lifecycle.SavedStateHandle
import com.saizad.mvvm.enums.DataState
import com.saizad.mvvm.model.IntPageDataModel
import com.saizad.mvvmexample.ApiRequestCodes
import com.saizad.mvvmexample.components.main.MainViewModel
import com.saizad.mvvmexample.di.main.MainEnvironment
import com.saizad.mvvmexample.models.ReqResUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class UsersViewModel @Inject constructor(
    mainEnvironment: MainEnvironment
) : MainViewModel(mainEnvironment){

    fun users(
        page: Int?,
        requestId: Int = ApiRequestCodes.LIST_OF_USERS
    ): Flow<DataState<IntPageDataModel<ReqResUser>>> {
        return flowData(api.users(page), requestId)
    }}