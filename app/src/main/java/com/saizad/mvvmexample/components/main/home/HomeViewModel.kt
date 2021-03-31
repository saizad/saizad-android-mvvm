package com.saizad.mvvmexample.components.main.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.saizad.mvvm.enums.DataState
import com.saizad.mvvm.model.DataModel
import com.saizad.mvvmexample.ApiRequestCodes
import com.saizad.mvvmexample.ApiRequestCodes.DELAYED_RESPONSE
import com.saizad.mvvmexample.ApiRequestCodes.DELETE_USER
import com.saizad.mvvmexample.ApiRequestCodes.LONG_DELAYED_RESPONSE
import com.saizad.mvvmexample.ApiRequestCodes.RESOURCE_NOT_FOUND
import com.saizad.mvvmexample.ApiRequestCodes.SHORT_DELAYED_RESPONSE
import com.saizad.mvvmexample.components.main.MainViewModel
import com.saizad.mvvmexample.di.main.MainEnvironment
import com.saizad.mvvmexample.models.ReqResUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
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
    ): Flow<DataState<DataModel<List<ReqResUser>>>> {
        return flowData(api.delayedResponse(delay), requestId)
    }

    fun logout(): LiveData<Void> {
        val mutableLiveData = MutableLiveData<Void>()
        viewModelScope.launch {
            currentUserType.logout()
        }
        return mutableLiveData
    }

    fun resourceNotFound(requestId: Int = RESOURCE_NOT_FOUND): Flow<DataState<Void>> {
        return flowData(api.resourceNotFound(), requestId)
    }

    fun delete(requestId: Int = DELETE_USER): Flow<DataState<Void>> {
        return flowData(api.delete(), requestId)
    }

    override fun showError(apiErrorData: ApiErrorData) {
        if(!apiErrorData.isThisRequest(RESOURCE_NOT_FOUND)) {
            super.showError(apiErrorData)
        }
    }

    override fun showLoading(loadingData: LoadingData) {
        if(!loadingData.isThisRequest(SHORT_DELAYED_RESPONSE, DELAYED_RESPONSE)) {
            super.showLoading(loadingData)
        }
    }
}