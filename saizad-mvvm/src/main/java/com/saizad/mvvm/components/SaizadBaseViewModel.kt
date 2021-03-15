package com.saizad.mvvm.components

import android.util.Log
import androidx.annotation.CallSuper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.google.android.gms.common.util.ArrayUtils
import com.sa.easyandroidform.ObjectUtils
import com.saizad.mvvm.ActivityResult
import com.saizad.mvvm.BaseNotificationModel
import com.saizad.mvvm.Environment
import com.saizad.mvvm.NotifyOnce
import com.saizad.mvvm.model.DataModel
import com.saizad.mvvm.model.ErrorModel
import com.saizad.mvvm.model.IntPageDataModel
import com.shopify.livedataktx.SingleLiveData
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.BehaviorSubject
import retrofit2.Response
import sa.zad.easyretrofit.observables.NeverErrorObservable
import sa.zad.pagedrecyclerlist.ConstraintLayoutList

abstract class SaizadBaseViewModel(
    val environment: Environment,
    val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val activityResult: BehaviorSubject<ActivityResult<*>> =
        environment.activityResultBehaviorSubject
    protected var notification: BehaviorSubject<NotifyOnce<*>> =
        environment.notificationBehaviorSubject

    protected var disposable = CompositeDisposable()
    val loadingLiveData = SingleLiveData<LoadingData>()
    val errorLiveData = SingleLiveData<ErrorData>()
    val apiErrorLiveData = SingleLiveData<ApiErrorData>()
    var currentUser = MutableLiveData<Any>()

    fun <T : BaseNotificationModel?> notificationListener(notificationType: Array<String>?): LiveData<T> {
        val notificationModelMutableLiveData = MutableLiveData<T>()
        disposable.add(
            notification
                .filter {
                    ArrayUtils.contains(notificationType, it.type)
                }
                .filter { !it.isRead }
                .observeOn(AndroidSchedulers.mainThread())
                .map { it.notificationModel as T }
                .subscribe({
                    notificationModelMutableLiveData.setValue(it)
                }) {

                }
        )
        return notificationModelMutableLiveData
    }

    fun <T : BaseNotificationModel?> notificationListener(notificationType: String): LiveData<T> {
        val types = arrayOf(notificationType)
        return notificationListener(types)
    }

    protected fun <M> pagedLiveData(
        observable: NeverErrorObservable<IntPageDataModel<M>>,
        callback: ConstraintLayoutList.CallBack<IntPageDataModel<M>>,
        errorCallback: Throwable.() -> Unit = {},
        requestId: Int
    ): LiveData<IntPageDataModel<M>> {
        val mutableLiveData = MutableLiveData<IntPageDataModel<M>>()
        request(observable, requestId)
            .doOnError { errorCallback.invoke(it) }
            .subscribe {
                callback.call(it)
            }
        return mutableLiveData
    }

    fun liveDataNoResponse(
        observable: NeverErrorObservable<Void>, requestId: Int
    ): LiveData<Void?> {
        val mutableLiveData = MutableLiveData<Void?>()
        request(observable, requestId) {
            mutableLiveData.setValue(this.body())
        }.subscribe()
        return mutableLiveData
    }

    open fun <M> liveDataRequestNoEnvelope(
        observable: NeverErrorObservable<M>,
        requestId: Int,
        errorResponse: Response<M>.() -> Unit = {}
    ): LiveData<M> {
        val mutableLiveData = MutableLiveData<M>()
        apiRequestNoEnvelope(observable, requestId, errorResponse)
            .subscribe {
                mutableLiveData.setValue(it)
            }
        return mutableLiveData
    }

    fun <M> liveData(observable: NeverErrorObservable<DataModel<M>>, requestId: Int): LiveData<M> {
        val mutableLiveData = MutableLiveData<M>()
        apiRequest(observable, requestId)
            .subscribe {
                mutableLiveData.setValue(it.data)
            }
        return mutableLiveData
    }

    fun <M> apiRequest(
        observable: NeverErrorObservable<DataModel<M>>, requestId: Int
    ): Observable<DataModel<M>> {
        return request(observable, requestId)
    }

    fun <M> apiRequestNoEnvelope(
        observable: NeverErrorObservable<M>, requestId: Int,
        errorResponse: Response<M>.() -> Unit = {}
    ): Observable<M> {
        return request(observable, requestId, errorResponse = errorResponse)
    }

    fun <M> request(
        observable: NeverErrorObservable<M>,
        requestId: Int,
        response: Response<M>.() -> Unit = {},
        errorResponse: Response<M>.() -> Unit = {}
    ): Observable<M> {
        Log.d("sadfasdfasdfa", "true -- Request Id -> $requestId")
        shootLoading(true, requestId)
        Log.d("sadfasdfasdfa-post", "true -- Request Id -> $requestId")
        return observable
            .successResponse { response.invoke(it) }
            .timeoutException { shootError(it, requestId) }
            .connectionException { shootError(it, requestId) }
            .failedResponse {
                errorResponse.invoke(it)
            }
            .apiException({
                it?.let {
                    shootError(it, requestId)
                }
            }, ErrorModel::class.java)
            .exception {
                shootError(it, requestId)
            }
            .doFinally {
                Log.d("sadfasdfasdfa", "false -- Request Id -> $requestId")
                shootLoading(false, requestId) }
    }

    fun activityResult(requestCode: Int): Observable<ActivityResult<*>> {
        return activityResult
            .filter { it.isOk }
            .filter {
                it.isRequestCode(requestCode)
            }
            .doOnNext {
                activityResult.onNext(
                    ActivityResult<Any>(3321235, 1, null)
                )
            }
    }

    fun <V> activityResult(requestCode: Int, cast: Class<V>): Observable<V> {
        return activityResult(requestCode)
            .map { it.value }
            .cast(cast)
    }

    fun onNavigationResult(requestCode: Int): MutableLiveData<ActivityResult<*>> {
        val activityResultLiveData =
            MutableLiveData<ActivityResult<*>>()
        disposable.add(activityResult(requestCode)
            .subscribe({
                activityResultLiveData.setValue(it)
            }
            ) {}
        )
        return activityResultLiveData
    }

    fun <V> onNavigationResult(requestCode: Int, cast: Class<V>): MutableLiveData<V> {
        val activityResultLiveData = MutableLiveData<V>()
        disposable.add(activityResult(requestCode, cast)
            .subscribe(
                { activityResultLiveData.setValue(it) }
            ) {}
        )
        return activityResultLiveData
    }

    @CallSuper
    override fun onCleared() {
        //Todo temp fix can't locate bug
        if (ObjectUtils.isNotNull(disposable)) {
            disposable.dispose()
        }
        super.onCleared()
    }

    @CallSuper
    open fun onDestroyView() {
        disposable.dispose()
    }

    @CallSuper
    open fun onViewCreated() {
        disposable = CompositeDisposable()
        disposable.add(
            environment.currentUser.observable()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    currentUser.setValue(it)
                }
        )
    }

    protected fun shootError(errorModel: ErrorModel, id: Int) {
        val value = ApiErrorData(ApiErrorException(errorModel), id)
        apiErrorLiveData.postValue(value)
    }

    protected fun shootError(throwable: Throwable, id: Int) {
        val value = ErrorData(throwable, id)
        errorLiveData.postValue(value)
    }

    protected fun shootLoading(loading: Boolean, id: Int) {
        val value = LoadingData(loading, id)
        loadingLiveData.postValue(value)
    }

    fun navigationFragmentResult(activityResult: ActivityResult<*>) {
        environment.activityResultBehaviorSubject.onNext(activityResult)
    }

    abstract class LiveDataStatus(var id: Int) {
        fun isThisRequest(id: Int): Boolean {
            return this.id == id
        }
    }

    class ApiErrorData(val apiErrorException: ApiErrorException, id: Int) :
        LiveDataStatus(id)

    class ErrorData(val throwable: Throwable, id: Int) : LiveDataStatus(id)

    class LoadingData(val isLoading: Boolean, id: Int) : LiveDataStatus(id)

    class ApiErrorException(val errorModel: ErrorModel) :
        Exception(Throwable(errorModel.error.message))
}