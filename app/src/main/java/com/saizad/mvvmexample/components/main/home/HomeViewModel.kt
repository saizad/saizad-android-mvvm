package com.saizad.mvvmexample.components.main.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.saizad.mvvm.enums.DataState
import com.saizad.mvvmexample.ApiRequestCodes.DELAYED_RESPONSE
import com.saizad.mvvmexample.ApiRequestCodes.RESOURCE_NOT_FOUND
import com.saizad.mvvmexample.components.main.MainViewModel
import com.saizad.mvvmexample.di.main.MainEnvironment
import com.saizad.mvvmexample.models.ReqResUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    environment: MainEnvironment,
    savedStateHandle: SavedStateHandle
) : MainViewModel(environment, savedStateHandle) {

    fun delayed(
        delay: Int,
        requestId: Int = DELAYED_RESPONSE
    ): LiveData<DataState<List<ReqResUser>>> {
        return liveData(api.delayedResponse(delay), requestId)
    }

    fun logout(): LiveData<Void> {
        val mutableLiveData = MutableLiveData<Void>()
        viewModelScope.launch {
            currentUserType.logout()
        }
        return mutableLiveData
    }

    fun resourceNotFound(requestId: Int = RESOURCE_NOT_FOUND): LiveData<DataState<Void?>> {
        return liveDataNoResponse(api.resourceNotFound(), requestId)
    }
}