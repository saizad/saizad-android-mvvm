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
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import com.saizad.mvvm.LoadingDialog
import com.saizad.mvvm.SaizadLocation.GPSOffException
import com.saizad.mvvm.components.SaizadBaseViewModel
import com.saizad.mvvm.components.SaizadBaseViewModel.*
import io.reactivex.disposables.CompositeDisposable
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
        return ViewModelProvider(appLifecycleDelegate.viewModelStoreOwner()).get(viewModel)
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

    override fun requestError(errorData: ErrorData) {
        val handle =
            baseLifecycleCallBack.serverError(errorData.throwable, errorData.id)
        if (!handle) {
            showAlertDialogOk("Error", errorData.throwable.message!!)
        }
    }

    override fun requestApiError(apiErrorData: ApiErrorData) {
        val error = apiErrorData.apiErrorException.errorModel.error
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
            ) { _: DialogInterface?, which: Int ->
                liveData.setValue(which)
            }
            .setNegativeButton(
                negativeName
            ) { _: DialogInterface?, which: Int ->
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

        viewModel.errorLiveData.observe(
            appLifecycleDelegate.lifecycleOwner,
            Observer {
                baseLifecycleCallBack.requestError(it)
            }
        )
        viewModel.apiErrorLiveData.observe(
            appLifecycleDelegate.lifecycleOwner,
            Observer {
                baseLifecycleCallBack.requestApiError(it)
            }
        )
        viewModel.loadingLiveData.observe(
            appLifecycleDelegate.lifecycleOwner,
            Observer {
                baseLifecycleCallBack.requestLoading(it)
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