package com.saizad.mvvm.components

import androidx.annotation.CallSuper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.sa.easyandroidform.ObjectUtils
import com.saizad.mvvm.ActivityResult
import com.saizad.mvvm.BaseNotificationModel
import com.saizad.mvvm.Environment
import com.saizad.mvvm.NotifyOnce
import com.saizad.mvvm.enums.DataState
import com.saizad.mvvm.model.BaseApiError
import com.saizad.mvvm.model.ErrorModel
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject
import kotlinx.coroutines.channels.ProducerScope
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import sa.zad.easyretrofit.observables.NeverErrorObservable
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

    fun <T : BaseNotificationModel> notificationListener(notificationType: Array<String>): LiveData<T> {
        val notificationModelMutableLiveData = MutableLiveData<T>()
        disposable.add(
            notification
                .filter {
                    notificationType.contains(it.type)
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

    fun <T : BaseNotificationModel> notificationListener(notificationType: String): LiveData<T> {
        val types = arrayOf(notificationType)
        return notificationListener(types)
    }

    fun <M> liveData(
        observable: NeverErrorObservable<M>,
        requestId: Int
    ): Flow<DataState<M>> {
        return flowData(observable, requestId, ErrorModel::class.java)
    }

    open fun <M, E: BaseApiError> flowData(
        observable: NeverErrorObservable<M>,
        requestId: Int,
        eClass: Class<E>,
    ): Flow<DataState<M>> {

        val block: suspend ProducerScope<DataState<M>>.() -> Unit = {
            val request = baseRequest(observable, requestId, eClass)
                .subscribe {
                    when (it) {
                        is DataState.Success -> {
                            offer(it)
                        }
                        is DataState.Loading -> {
                            offer(it)
                            if (!it.isLoading) {
                                close()
                            }
                        }
                        is DataState.Error -> {
                            offer(it)
                        }
                        is DataState.ApiError -> {
                            offer(it)
                        }
                    }
                }

            disposable.add(request)

            awaitClose {
                request.dispose()
            }
        }
        return callbackFlow(block)
    }

    fun <M, E : BaseApiError> baseRequest(
        observable: NeverErrorObservable<M>,
        requestId: Int,
        eClass: Class<E>
    ): Observable<DataState<M>> {
        val publishSubject = BehaviorSubject.create<DataState<M>>()
        val loadingData = LoadingData(true, requestId)
        showLoading(loadingData)
        publishSubject.onNext(DataState.Loading(true))
        observable
            .successResponse {
                publishSubject.onNext(DataState.Success(it.body()))
            }
            .timeoutException { shootError(publishSubject, it, requestId) }
            .connectionException { shootError(publishSubject, it, requestId) }
            .apiException({
                if (it != null) {
                    shootError(publishSubject, it, requestId)
                }
            }, eClass)
            .exception {
                shootError(publishSubject, it, requestId)
            }
            .doFinally {
                loadingData.isLoading = false
                showLoading(loadingData)
                publishSubject.onNext(DataState.Loading(false))
            }.subscribe()
        return publishSubject
    }

    open fun showLoading(loadingData: LoadingData){
        loadingSubject.onNext(loadingData)
    }

    private fun <M> shootError(
        publishSubject: BehaviorSubject<DataState<M>>,
        errorModel: BaseApiError,
        id: Int
    ) {
        val apiErrorException = ApiErrorException(errorModel)
        showError(ApiErrorData(apiErrorException, id))
        publishSubject.onNext(DataState.ApiError(apiErrorException))
    }

    open fun showError(apiErrorData: ApiErrorData){
        apiErrorSubject.onNext(apiErrorData)
    }

    private fun <M> shootError(
        publishSubject: BehaviorSubject<DataState<M>>,
        throwable: Throwable, id: Int
    ) {
        val errorData = ErrorData(throwable, id)
        errorSubject.onNext(errorData)
        publishSubject.onNext(DataState.Error(throwable))
    }

    open fun showError(errorData: ErrorData){
        errorSubject.onNext(errorData)
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
        open fun isThisRequest(vararg ids: Int): Boolean {
            return ids.contains(id)
        }
    }

    class ApiErrorData(val apiErrorException: ApiErrorException, id: Int) :
        LiveDataStatus(id)

    class ErrorData(val throwable: Throwable, id: Int) : LiveDataStatus(id)

    class LoadingData(var isLoading: Boolean, id: Int) : LiveDataStatus(id)

    class ApiErrorException(val errorModel: BaseApiError) :
        Exception(Throwable(errorModel.message()))
}