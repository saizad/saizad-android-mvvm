package com.saizad.mvvmexample.components

import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import com.google.gson.Gson
import com.saizad.mvvm.Environment
import com.saizad.mvvm.components.SaizadBaseViewModel
import com.saizad.mvvmexample.models.ApiError
import retrofit2.Response
import sa.zad.easyretrofit.observables.NeverErrorObservable
import javax.inject.Inject

abstract class MVVMExampleViewModel(
    environment: Environment,
    savedStateHandle: SavedStateHandle
) : SaizadBaseViewModel(environment, savedStateHandle){

    @Inject
    lateinit var gson: Gson

    fun <M> mVVMExampleRequest(
        observable: NeverErrorObservable<M>,
        requestId: Int,
        errorResponse: ApiError.() -> Unit = {
            shootError(Throwable(this.error), 1)
        }
    ): LiveData<M> {
        return super.liveDataRequestNoEnvelope(observable, requestId) {
            val apiError = gson.fromJson(this.errorBody()!!.string(), ApiError::class.java)
            errorResponse.invoke(apiError)
        }
    }
}