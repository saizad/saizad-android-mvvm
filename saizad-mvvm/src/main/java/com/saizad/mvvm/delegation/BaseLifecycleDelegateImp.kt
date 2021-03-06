package com.saizad.mvvm.delegation

import android.content.DialogInterface
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.annotation.CallSuper
import androidx.annotation.DrawableRes
import androidx.annotation.IdRes
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.*
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import com.saizad.mvvm.ActivityResult
import com.saizad.mvvm.LoadingDialog
import com.saizad.mvvm.SaizadLocation.GPSOffException
import com.saizad.mvvm.components.SaizadBaseViewModel
import com.saizad.mvvm.components.SaizadBaseViewModel.*
import com.saizad.mvvm.di.AssistedFactory
import com.saizad.mvvm.di.AssistedFactory.Companion.provideFactory
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import rx.functions.Action1

abstract class BaseLifecycleDelegateImp<V : SaizadBaseViewModel, CB : BaseCB<V>>(
    private val baseLifecycleCallBack: BaseLifecycleCallBack,
    protected val appLifecycleDelegate: CB,
    val tag: String
) : BaseLifecycleDelegate {

    lateinit var compositeDisposable: CompositeDisposable
    lateinit var viewModel: V


    val loadingDialog: LoadingDialog by lazy {
        LoadingDialog(appLifecycleDelegate.context())
    }

    private fun getFragmentViewModel(viewModel: Class<V>): V {
        val assistedFactory = appLifecycleDelegate.viewModelProviderFactory()
        val owner = appLifecycleDelegate.viewModelStoreOwner()
        val viewModelProvider = if (assistedFactory != null) {
            val provideFactory =
                provideFactory(assistedFactory, appLifecycleDelegate.bundle()!!)
            ViewModelProvider(owner, provideFactory)
        } else {
            ViewModelProvider(owner)
        }
        return viewModelProvider[viewModel]
    }

    override fun showLongToast(text: CharSequence) {
        showToast(text, Toast.LENGTH_LONG)
    }

    override fun showShortToast(value: Int) {
        showToast(value.toString(), Toast.LENGTH_SHORT)
    }

    override fun showShortToast(text: CharSequence) {
        showToast(text, Toast.LENGTH_SHORT)
    }

    override fun showToast(text: CharSequence, toastLength: Int) {
        Toast.makeText(appLifecycleDelegate.context(), text, toastLength).show()
    }

    override fun log(integer: Int) {
        log(integer.toString())
    }

    override fun log(string: String) {
        Log.i(tag, string)
    }

    override val navigationFragmentResult: BehaviorSubject<ActivityResult<*>>
        get() = appLifecycleDelegate.environment().navigationFragmentResult()


    override val schedulerProviderUI: Scheduler
        get() = AndroidSchedulers.mainThread()


    override val schedulerProviderIO: Scheduler
        get() = Schedulers.io()

    override fun requestError(errorData: ErrorData) {
        val handle =
            baseLifecycleCallBack.serverError(errorData.throwable, errorData.id)
        if (!handle) {
            showAlertDialogOk("Error", errorData.throwable.message!!)
        }
    }

    override fun requestApiError(apiErrorData: ApiErrorData) {
        val error = apiErrorData.apiErrorException.error
        val handel = baseLifecycleCallBack.serverError(
            apiErrorData.apiErrorException, apiErrorData.id
        )
        if (!handel) {
            showAlertDialogOk(error.error, error.message)
        }
    }

    override fun requestLoading(loadingData: LoadingData) {
        loadingDialog.show(loadingData.isLoading)
    }

    override fun showLoading(show: Boolean) {
        loadingDialog.show(show)
    }

    override fun serverError(throwable: Throwable, requestId: Int): Boolean {
        return false
    }

    override fun showAlertDialogOk(
        title: String,
        message: String,
        cancelAble: Boolean
    ): LiveData<Int> {
        val liveData = MutableLiveData<Int>()
        AlertDialog.Builder(appLifecycleDelegate.context())
            .setTitle(title)
            .setMessage(message)
            .setCancelable(cancelAble)
            .setPositiveButton(
                android.R.string.ok
            ) { dialog: DialogInterface?, which: Int ->
                liveData.setValue(which)
            }
            .show()
        return liveData
    }

    override fun showAlertDialogOk(
        title: String,
        message: String
    ): LiveData<Int> {
        return showAlertDialogOk(title, message, false)
    }

    override fun showAlertDialogYesNo(
        title: String,
        message: String,
        @DrawableRes icon: Int
    ): LiveData<Int> {
        return showAlertDialogYesNo(
            title,
            message,
            icon,
            appLifecycleDelegate.context().getString(android.R.string.yes),
            appLifecycleDelegate.context().getString(android.R.string.no)
        )
    }

    override fun showAlertDialogYesNo(
        title: String,
        message: String,
        @DrawableRes icon: Int,
        positiveName: String,
        negativeName: String
    ): LiveData<Int> {
        val liveData = MutableLiveData<Int>()
        AlertDialog.Builder(appLifecycleDelegate.context())
            .setTitle(title)
            .setMessage(message)
            .setIcon(icon)
            .setPositiveButton(
                positiveName
            ) { dialog: DialogInterface?, which: Int ->
                liveData.setValue(which)
            }
            .setNegativeButton(
                negativeName
            ) { dialog: DialogInterface?, which: Int ->
                liveData.setValue(which)
            }
            .show()
        return liveData
    }

    override fun requestLocation(locationAction: Action1<Location>) {
        loadingDialog.show(true)
        appLifecycleDelegate.appLocation()
            .getLastLocation({ location: Location ->
                loadingDialog.show(false)
                locationAction.call(location)
            }) { throwable: Throwable ->
                loadingDialog.show(false)
                var title = "Error"
                var message = throwable.message
                if (throwable is SecurityException) {
                    title = "Permission Not Granted"
                    message = "Please provide location permission from the app settings"
                } else if (throwable is GPSOffException) {
                    title = "GPS is OFF"
                    message = throwable.message
                }
                showAlertDialogOk(title, message!!, true)
            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        log("onCreate")
        viewModel = getFragmentViewModel(appLifecycleDelegate.viewModelClassType)
        //compositeDisposable = new CompositeDisposable();
    }

    @CallSuper
    override fun onResume() {
        log("onResume")
    }

    @CallSuper
    override fun onStart() {
        log("onStart")
    }

    @CallSuper
    override fun onPause() {
        log("onPause")
    }

    @CallSuper
    override fun onStop() {
        log("onStop")
    }

    @CallSuper
    override fun onDestroy() {
        log("onDestroy")
        try {
            compositeDisposable.dispose()
        } catch (e: Exception) {
            log(e.toString())
        }
    }

    @CallSuper
    override fun onViewReady() {
        viewModel.onViewCreated()
        compositeDisposable = CompositeDisposable()
        log("onViewReady")
        viewModel.errorLiveData().observe(
            appLifecycleDelegate.lifecycleOwner,
            Observer { errorData: ErrorData ->
                if (!errorData.isDiscarded) {
                    baseLifecycleCallBack.requestError(errorData)
                    errorData.discard()
                }
            }
        )
        viewModel.apiErrorLiveData().observe(
            appLifecycleDelegate.lifecycleOwner,
            Observer { apiErrorData: ApiErrorData ->
                if (!apiErrorData.isDiscarded) {
                    baseLifecycleCallBack.requestApiError(apiErrorData)
                    apiErrorData.discard()
                }
            }
        )
        viewModel.loadingLiveData().observe(
            appLifecycleDelegate.lifecycleOwner,
            Observer { loadingData: LoadingData ->
                if (!loadingData.isDiscarded) {
                    baseLifecycleCallBack.requestLoading(loadingData)
                    loadingData.discard()
                }
            }
        )
    }

    override fun viewModel(): SaizadBaseViewModel {
        return viewModel
    }

    override fun compositeDisposable(): CompositeDisposable {
        return compositeDisposable
    }

    override fun openFragment(@IdRes fragment: Int, bundle: Bundle?, navOptions: NavOptions?) {
        navController().navigate(fragment, bundle, navOptions)
    }

    override fun navController(): NavController {
        return appLifecycleDelegate.navController()
    }

}