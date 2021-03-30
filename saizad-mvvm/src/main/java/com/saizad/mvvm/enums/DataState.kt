package com.saizad.mvvm.enums

import com.saizad.mvvm.components.SaizadBaseViewModel

sealed class DataState<out R> {

    data class Success<out T>(val data: T?) : DataState<T>()
    data class ApiError(val apiErrorException: SaizadBaseViewModel.ApiErrorException) :
        DataState<Nothing>()

    data class Error(val throwable: Throwable) : DataState<Nothing>()
    data class Loading(val isLoading: Boolean) : DataState<Nothing>()
}