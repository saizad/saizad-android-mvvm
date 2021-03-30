package com.saizad.mvvmexample.components

import androidx.lifecycle.SavedStateHandle
import com.google.gson.Gson
import com.saizad.mvvm.Environment
import com.saizad.mvvm.components.SaizadBaseViewModel
import com.saizad.mvvm.enums.DataState
import com.saizad.mvvm.model.BaseApiError
import com.saizad.mvvmexample.models.ApiError
import kotlinx.coroutines.flow.Flow
import retrofit2.Response
import sa.zad.easyretrofit.observables.NeverErrorObservable
import javax.inject.Inject

abstract class MVVMExampleViewModel(
    environment: Environment,
    savedStateHandle: SavedStateHandle
) : SaizadBaseViewModel(environment, savedStateHandle){

    @Inject
    lateinit var gson: Gson


    fun <M> flowData(
        observable: NeverErrorObservable<M>,
        requestId: Int
    ): Flow<DataState<M>> {
        return flowData(observable, requestId, eClass =  ApiError::class.java)
    }
}