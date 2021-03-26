package com.saizad.mvvm.components

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
import com.saizad.mvvm.enums.DataState
import com.saizad.mvvm.model.DataModel
import com.saizad.mvvm.model.ErrorModel
import com.saizad.mvvm.model.IntPageDataModel
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject
import retrofit2.Response
import sa.zad.easyretrofit.observables.NeverErrorObservable
import sa.zad.pagedrecyclerlist.ConstraintLayoutList
import javax.inject.Inject
import javax.inject.Named

abstract class SaizadBaseViewModel(
    val environment: Environment,
    val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val activityResult: BehaviorSubject<ActivityResult<*>> =
        environment.activityResultBehaviorSubject

    private var notification: BehaviorSubject<NotifyOnce<*>> =
        environment.notificationBehaviorSubject

    protected var disposable = CompositeDisposable()

    @Inject
    @Named("loadingState")
    lateinit var loadingSubject: PublishSubject<LoadingData>

    val errorSubject = PublishSubject.create<ErrorData>()
    val apiErrorSubject = PublishSubject.create<ApiErrorData>()

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
    ): LiveData<DataState<IntPageDataModel<M>>> {
        val mutableLiveData = MutableLiveData<DataState<IntPageDataModel<M>>>()
        request(observable, requestId)
            .doOnError { errorCallback.invoke(it) }
            .subscribe {
                if (it is DataState.Success<IntPageDataModel<M>>) {
                    callback.call(it.data)
                }
            }
        return mutableLiveData
    }

    fun liveDataNoResponse(
        observable: NeverErrorObservable<Void>, requestId: Int
    ): LiveData<DataState<Void?>> {
        val mutableLiveData = MutableLiveData<DataState<Void?>>()
        request(observable, requestId)
            .subscribe {
                if (it is DataState.Success) {
                    mutableLiveData.postValue(it)
                } else {
                    routeDataSet(mutableLiveData, it)
                }
            }
        return mutableLiveData
    }

    open fun <M> liveDataRequestNoEnvelope(
        observable: NeverErrorObservable<M>,
        requestId: Int,
        errorResponse: Response<M>.() -> Unit = {}
    ): LiveData<DataState<M>> {
        val mutableLiveData = MutableLiveData<DataState<M>>()
        apiRequestNoEnvelope(observable, requestId, errorResponse)
            .subscribe {
                mutableLiveData.setValue(it)
            }
        return mutableLiveData
    }

    fun <M> liveData(
        observable: NeverErrorObservable<DataModel<M>>,
        requestId: Int
    ): LiveData<DataState<M>> {
        val mutableLiveData = MutableLiveData<DataState<M>>()
        apiRequest(observable, requestId)
            .subscribe {
                when (it) {
                    is DataState.Success<DataModel<M>> -> {
                        mutableLiveData.value = DataState.Success(it.data.data)
                    }
                    is DataState.Loading -> {
                        mutableLiveData.postValue(it)
                    }
                    is DataState.Error -> {
                        mutableLiveData.postValue(it)
                    }
                    is DataState.ApiError -> {
                        mutableLiveData.postValue(it)
                    }
                }
            }
        return mutableLiveData
    }

    fun <M> apiRequest(
        observable: NeverErrorObservable<DataModel<M>>, requestId: Int
    ): Observable<DataState<DataModel<M>>> {
        return request(observable, requestId)
    }

    fun <M> apiRequestNoEnvelope(
        observable: NeverErrorObservable<M>, requestId: Int,
        errorResponse: Response<M>.() -> Unit = {}
    ): Observable<DataState<M>> {
        return request(observable, requestId, errorResponse = errorResponse)
    }

    fun <M> request(
        observable: NeverErrorObservable<M>,
        requestId: Int,
        response: Response<M>.() -> Unit = {},
        errorResponse: Response<M>.() -> Unit = {}
    ): Observable<DataState<M>> {
        val publishSubject = BehaviorSubject.create<DataState<M>>()
        val loadingData = LoadingData(true, requestId)
        loadingSubject.onNext(loadingData)
        publishSubject.onNext(DataState.Loading(true))
        observable
            .successResponse {
                response.invoke(it)
                publishSubject.onNext(DataState.Success(it.body()!!))
            }
            .timeoutException { shootError(publishSubject, it, requestId) }
            .connectionException { shootError(publishSubject, it, requestId) }
            .failedResponse {
                errorResponse.invoke(it)
            }
            .apiException({
                it?.let {
                    shootError(publishSubject, it, requestId)
                }
            }, ErrorModel::class.java)
            .exception {
                shootError(publishSubject, it, requestId)
            }
            .doFinally {
                loadingData.isLoading = false
                loadingSubject.onNext(loadingData)
                publishSubject.onNext(DataState.Loading(false))
            }.subscribe()
        return publishSubject
    }

    private fun <M> shootError(
        publishSubject: BehaviorSubject<DataState<M>>,
        errorModel: ErrorModel,
        id: Int
    ) {
        val apiErrorException = ApiErrorException(errorModel)
        val value = ApiErrorData(apiErrorException, id)
        apiErrorSubject.onNext(value)
        publishSubject.onNext(DataState.ApiError(apiErrorException))
    }

    private fun <M> shootError(
        publishSubject: BehaviorSubject<DataState<M>>,
        throwable: Throwable, id: Int
    ) {
        val value = ErrorData(throwable, id)
        errorSubject.onNext(value)
        publishSubject.onNext(DataState.Error(throwable))
    }

    private fun <M> routeDataSet(
        mutableLiveData: MutableLiveData<DataState<M>>,
        dataState: DataState<M>
    ) {
        when (dataState) {
            is DataState.Loading -> {
                mutableLiveData.value = dataState
            }
            is DataState.Error -> {
                mutableLiveData.value = dataState
            }
            is DataState.ApiError -> {
                mutableLiveData.postValue(dataState)
            }
        }
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

    class LoadingData(var isLoading: Boolean, id: Int) : LiveDataStatus(id)

    class ApiErrorException(val errorModel: ErrorModel) :
        Exception(Throwable(errorModel.error.message))
}