package com.saizad.mvvmexample.components.main.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import com.saizad.mvvmexample.ApiRequestCodes
import com.saizad.mvvmexample.components.main.MainViewModel
import com.saizad.mvvmexample.di.main.MainEnvironment
import com.saizad.mvvmexample.models.ReqResUser
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    environment: MainEnvironment,
    savedStateHandle: SavedStateHandle
) : MainViewModel(environment, savedStateHandle) {

    fun delayed(
        delay: Int,
        requestId: Int = ApiRequestCodes.DELAYED_RESPONSE
    ): LiveData<List<ReqResUser>> {
        return liveData(api.delayedResponse(delay), requestId)
    }

    fun logout(): LiveData<Void>{
        val mutableLiveData = MutableLiveData<Void>()
        currentUserType.logout()
        return mutableLiveData
    }
}