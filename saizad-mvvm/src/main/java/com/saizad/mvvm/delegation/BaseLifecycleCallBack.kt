package com.saizad.mvvm.delegation

import com.saizad.mvvm.components.SaizadBaseViewModel.*

interface BaseLifecycleCallBack {
    fun requestError(errorData: ErrorData)
    fun requestApiError(apiErrorData: ApiErrorData)
    fun requestLoading(loadingData: LoadingData)
    fun serverError(throwable: Throwable, requestId: Int): Boolean
}