package com.saizad.mvvm.components

import android.os.Handler
import android.os.Looper
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
    protected var notification: BehaviorSubject<NotifyOnce<*>> = environment.notificationBehaviorSubject

    protected var disposable = CompositeDisposable()
    private val loadingLiveData = MutableLiveData<LoadingData>()
    private val errorLiveData = MutableLiveData<ErrorData>()
    private val apiErrorLiveData = MutableLiveData<ApiErrorData>()
    var currentUser = MutableLiveData<Any>()

    fun <T : BaseNotificationModel?> notificationListener(notificationType: Array<String>?): LiveData<T> {
        val notificationModelMutableLiveData = MutableLiveData<T>()
        disposable.add(
            notification
                .filter { notifyOnce: NotifyOnce<*> ->
                    ArrayUtils.contains(notificationType, notifyOnce.type)
                }
                .filter { notifyOnce: NotifyOnce<*> -> !notifyOnce.isRead }
                .observeOn(AndroidSchedulers.mainThread())
                .map { notifyOnce: NotifyOnce<*> -> notifyOnce.notificationModel as T }
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
        request(observable, requestId) {
            val data = this.body()!!
            callback.call(data)
            mutableLiveData.setValue(data)
        }.subscribe()
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

    fun <M> liveData(observable: NeverErrorObservable<DataModel<M>>, requestId: Int): LiveData<M> {
        val mutableLiveData = MutableLiveData<M>()
        apiRequest(observable, requestId)
            .subscribe { mDataModel: DataModel<M> ->
                mutableLiveData.setValue(
                    mDataModel.data
                )
            }
        return mutableLiveData
    }

    fun <M> apiRequest(
        observable: NeverErrorObservable<DataModel<M>>, requestId: Int
    ): Observable<DataModel<M>> {
        return request(
            observable,
            requestId
        )
    }

    fun <M> request(
        observable: NeverErrorObservable<M>, requestId: Int, response: Response<M>.() -> Unit = {}
    ): Observable<M> {
        shootLoading(true, requestId)
        return observable
            .successResponse { response.invoke(it) }
            .timeoutException { shootError(it, requestId) }
            .connectionException { shootError(it, requestId) }
            .failedResponse { dataModelResponse: Response<M>? -> }
            .apiException({
                shootError(it, requestId)
            }, ErrorModel::class.java)
            .exception {
                shootError(it, requestId)
            }
            .doFinally { shootLoading(false, requestId) }
    }

    fun activityResult(requestCode: Int): Observable<ActivityResult<*>> {
        return activityResult
            .filter { it.isOk }
            .filter { result: ActivityResult<*> ->
                result.isRequestCode(requestCode)
            }
            .doOnNext { result: ActivityResult<*>? ->
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
            ) { }
        )
        return activityResultLiveData
    }

    fun <V> onNavigationResult(requestCode: Int, cast: Class<V>): MutableLiveData<V> {
        val activityResultLiveData = MutableLiveData<V>()
        disposable.add(activityResult(requestCode, cast)
            .subscribe(
                { value: V ->
                    activityResultLiveData.setValue(
                        value
                    )
                }
            ) { }
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
    fun onDestroyView() {
        disposable.dispose()
    }

    fun errorLiveData(): LiveData<ErrorData> {
        return errorLiveData
    }

    fun loadingLiveData(): MutableLiveData<LoadingData> {
        return loadingLiveData
    }

    fun apiErrorLiveData(): MutableLiveData<ApiErrorData> {
        return apiErrorLiveData
    }

    @CallSuper
    fun onViewCreated() {
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
        Handler(Looper.getMainLooper())
            .post { apiErrorLiveData.setValue(value) }
    }

    protected fun shootError(throwable: Throwable, id: Int) {
        val value = ErrorData(throwable, id)
        Handler(Looper.getMainLooper())
            .post { errorLiveData.setValue(value) }
    }

    protected fun shootLoading(loading: Boolean, id: Int) {
        val value = LoadingData(loading, id)
        Handler(Looper.getMainLooper())
            .post { loadingLiveData.setValue(value) }
    }

    abstract class LiveDataStatus(var id: Int) {
        fun isThisRequest(id: Int): Boolean {
            return this.id == id
        }

        val isDiscarded: Boolean
            get() = DISCARDED_ID == id

        fun discard() {
            id = DISCARDED_ID
        }

        companion object {
            private const val DISCARDED_ID = 3412341
        }

    }

    class ApiErrorData(val apiErrorException: ApiErrorException, id: Int) :
        LiveDataStatus(id)

    class ErrorData(val throwable: Throwable, id: Int) : LiveDataStatus(id)

    class LoadingData(val isLoading: Boolean, id: Int) : LiveDataStatus(id)

    class ApiErrorException(val errorModel: ErrorModel) :
        Exception(Throwable(errorModel.error.message)) {

        val error: ErrorModel.Error
            get() = errorModel.error

    }
}