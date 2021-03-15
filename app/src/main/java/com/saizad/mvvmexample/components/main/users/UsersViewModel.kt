package com.saizad.mvvmexample.components.main.users

import androidx.lifecycle.SavedStateHandle
import com.saizad.mvvm.model.IntPageDataModel
import com.saizad.mvvmexample.ApiRequestCodes
import com.saizad.mvvmexample.components.main.MainViewModel
import com.saizad.mvvmexample.di.main.MainEnvironment
import com.saizad.mvvmexample.models.ReqResUser
import dagger.hilt.android.lifecycle.HiltViewModel
import rx.functions.Action1
import sa.zad.pagedrecyclerlist.ConstraintLayoutList
import javax.inject.Inject

@HiltViewModel
class UsersViewModel @Inject constructor(
    mainEnvironment: MainEnvironment,
    savedStateHandle: SavedStateHandle
) : MainViewModel(mainEnvironment, savedStateHandle){


    fun users(
        page: Int?,
        callback: ConstraintLayoutList.CallBack<IntPageDataModel<ReqResUser>>,
        errorCallback: Throwable.() -> Unit = {},
        requestId: Int = ApiRequestCodes.LIST_OF_USERS
    ) {
        pagedLiveData(
            api.users(page), callback, errorCallback, requestId
        )
    }}